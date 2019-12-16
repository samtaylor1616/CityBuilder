package au.edu.staylor.citybuilder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import static au.edu.staylor.citybuilder.Type.*;

public class MapFragment extends Fragment {

    private SelectorFragment sFrag;
    private UserDetailsFragment uFrag;
    private MapAdapter adapter;
    private GameData game;
    private int width;
    private int height;
    private int positionChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        RecyclerView rv = view.findViewById(R.id.mapRecyclerView);

        game = GameData.get(getActivity());
        width = game.getWidth();
        height = game.getHeight();

        rv.setLayoutManager( new GridLayoutManager(
                getActivity(),
                height,
                GridLayoutManager.HORIZONTAL,
                false ));

        adapter = new MapAdapter();
        rv.setAdapter(adapter);

        return view;
    }

    public void updateAdapter() {
        adapter.notifyItemChanged( positionChange );
    }

    public void setSFrag(SelectorFragment frag) {
        this.sFrag = frag;
    }

    public void setUserFrag(UserDetailsFragment userFrag) { this.uFrag = userFrag; }

    private class MapDataVHolder extends RecyclerView.ViewHolder {
        private MapElement mapElement;
        private int row;
        private int col;
        private int position;

        private ImageView structure;
        private ImageView topLeft;
        private ImageView topRight;
        private ImageView bottomLeft;
        private ImageView bottomRight;

        public MapDataVHolder(LayoutInflater li, ViewGroup parent) {

            super( li.inflate(R.layout.grid_cell, parent, false) );

            int size = parent.getMeasuredHeight() / height + 1;
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            lp.width = size;
            lp.height = size;

            // Grab UI components
            structure = itemView.findViewById(R.id.structure);
            topLeft = itemView.findViewById(R.id.topLeft);
            topRight = itemView.findViewById(R.id.topRight);
            bottomLeft = itemView.findViewById(R.id.bottomLeft);
            bottomRight = itemView.findViewById(R.id.bottomRight);

            // Set up callbacks
            structure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positionChange = position;

                    Structure newStructure = sFrag.getCurrentlySelected();
                    if ( newStructure != null ) {

                        if (newStructure.getType() == DETAILS) {
                            // If the user choose to look at details
                            getDetails();
                        } else {
                            // Buys it and sets the new structure
                            if ( checkType(newStructure.getType()) || allowedToBuild(row, col)) {
                                buy(newStructure.getType());
                                newStructure(newStructure);
                            }
                            // Else they AREN'T ALLOWED TO BUILD
                        }
                    }
                }
            });
        }

        private boolean checkType(Type type) {
            return type != RESIDENTIAL && type != COMMERCIAL;
        }

        protected boolean allowedToBuild(int row, int col) {
            boolean north = canBuild(row-1, col);
            boolean east = canBuild(row, col+1);
            boolean south = canBuild(row+1, col);
            boolean west = canBuild(row, col-1);

            // We are allowed to build a house or commercial property
            // if there is a road adjacent to it
            return north || east || south || west;
        }

        private boolean canBuild(int row, int col) {
            boolean canBuild = false;
            try {
                Type type = game.get(row, col).getStructure().getType();
                if (type == ROAD) {
                    canBuild = true;
                }
            } catch (NullPointerException e) {
                // It is fine if the structure is null
            }
            return canBuild;
        }

        protected void buy(Type type) {
            int cost = game.getSettings().getCostFromType(type);
            int money = game.getMoney();

            // If the player has enough money they can buy
            if (money > cost) {
                game.buy(cost);
            } else {
                if(money >= 0) { // Only show GAME OVER once
                    GameOverFragment gameOver = new GameOverFragment();

                    gameOver.show(getActivity().getSupportFragmentManager(), "gameOver");
                }
                game.buy(cost);
            }
        }

        protected void getDetails() {
            // Only run if the user selects a map element with a structure on it
            if (structure.getDrawable() != null) {
                Context c = getActivity();

                // Activate Details page
                getActivity().startActivityForResult(DetailsActivity.getIntent(c, row, col, TypeToString(mapElement.getStructure().getType()), mapElement.getName(), mapElement.getImage()), MapActivity.REQUEST_DETAILS);
            }
        }

        protected void newStructure(Structure newStructure) {
            if (newStructure.getType() != Type.DELETE) {
                // If there isn't already a structure there
                if ( mapElement.isBuildable() && structure.getDrawable() == null ) {
                    changeStructureCount(newStructure,1);

                    mapElement.setStructure(newStructure);
                    structure.setImageResource(mapElement.getStructure().getDrawableId());
                    sFrag.resetSelected();
                    adapter.notifyItemChanged( getAdapterPosition() );

                    uFrag.updateMoney();
                }
            } else if (structure.getDrawable() != null) {
                // Else if there is a structure and the user selected to delete
                deleteStructure();
            }
        }

        protected void deleteStructure() {
            changeStructureCount(mapElement.getStructure(), -1);
            if (mapElement.getImage() != null) {
                mapElement.setImage(null);
            }
            mapElement.setStructure(null);
            adapter.notifyItemChanged( getAdapterPosition() );
            sFrag.resetSelected();
        }

        protected void changeStructureCount(Structure structure, int valueToChangeBy) {
            if (structure.getType() == COMMERCIAL) {
                game.changeCommercialCount(valueToChangeBy);
            } else if (structure.getType() == RESIDENTIAL) {
                game.changeResidentialCount(valueToChangeBy);
            }
        }

        public void bind( MapElement mapElement, int row, int col, int position ) {
            this.row = row;
            this.col = col;
            this.position = position;
            this.mapElement = mapElement;
            setImgResources(mapElement);

            // If there is a save photo for this map element set it
            if(mapElement.getImage() != null)
                structure.setImageBitmap(mapElement.getImage());
        }

        private void setImgResources(MapElement mapElement) {
            Structure newStructure = mapElement.getStructure();
            this.structure.setImageResource(0);
            if (newStructure != null) {
                this.structure.setImageResource(newStructure.getDrawableId());
            }

            topLeft.setImageResource( mapElement.getNorthWest() );
            topRight.setImageResource( mapElement.getNorthEast() );
            bottomLeft.setImageResource( mapElement.getSouthWest() );
            bottomRight.setImageResource( mapElement.getSouthEast() );
        }
    }

    private class MapAdapter extends RecyclerView.Adapter<MapDataVHolder> {

        @Override public int getItemCount() { return (width * height); }

        @Override
        public MapDataVHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
            LayoutInflater li = LayoutInflater.from( getActivity() );
            return new MapDataVHolder( li, parent );
        }

        @Override
        public void onBindViewHolder( MapDataVHolder vh, int position ) {
            int row = position % height;
            int col = position / height;

            vh.bind( game.get(row, col), row, col, position );
        }
    }
}
