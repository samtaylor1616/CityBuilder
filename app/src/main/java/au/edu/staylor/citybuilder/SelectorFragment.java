package au.edu.staylor.citybuilder;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static au.edu.staylor.citybuilder.Type.RESET;

public class SelectorFragment extends Fragment {

    private SelectorAdapter adapter;
    private StructureData structureData;
    private Structure currentlySelected;
    private ImageView currentlySelectedImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_selector, container, false);

        RecyclerView rv = view.findViewById(R.id.selectorRecyclerView);
        rv.setLayoutManager( new LinearLayoutManager( getActivity(), LinearLayout.HORIZONTAL, false));

        structureData = StructureData.get();
        currentlySelected = null;
        currentlySelectedImg = null;

        adapter = new SelectorAdapter();
        rv.setAdapter(adapter);

        return view;
    }

    public void setCurrentlySelected(Structure newStructure, ImageView newImg) {
        if (newStructure.getType() == RESET) {
            startActivity(MapActivity.getIntent(getActivity(), true));
        } else {
            if(currentlySelectedImg != null) {
                // If an image is already selected, reset the background back to transparent
                setTransparent(currentlySelectedImg);
            }

            currentlySelected = newStructure;

            // Set the image and set the background to yellow to show it is selected
            currentlySelectedImg = newImg;
            setYellow(currentlySelectedImg);
        }
    }

    public Structure getCurrentlySelected() { return currentlySelected; }
    public void resetSelected() {
        currentlySelected = null;
        if (currentlySelectedImg != null)
            // If an image is selected, reset the background back to transparent
            setTransparent(currentlySelectedImg);
    }


    private void setTransparent(ImageView icon) {
        icon.setBackgroundColor(Color.parseColor("#00FFFFFF"));
    }
    private void setYellow(ImageView icon) {
        icon.setBackgroundColor(Color.parseColor("#ffff00"));
    }

    private class SelectorDataVHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView label;
        private Structure structure;

        public SelectorDataVHolder(LayoutInflater li, ViewGroup parent) {
            super( li.inflate(R.layout.list_selection, parent, false) );

            icon = itemView.findViewById(R.id.icon);
            label = itemView.findViewById(R.id.title);

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentlySelected(structure, icon);
                }
            });
        }

        public void bind( Structure structure ) {
            this.structure = structure;
            if (structure != null) {
                setResources(this.structure);
            }
        }

        private void setResources(Structure structure) {
            if (icon != null) {
                setTransparent(icon);
            }

            icon.setImageResource(structure.getDrawableId());
            label.setText(structure.getLabel());

            if (!structure.equals(currentlySelected)) {
                // Set a transparent background
                setTransparent(icon);
            } else {
                // Otherwise the icon is selected and should be highlighted
                setYellow(icon);
            }
        }
    }

    private class SelectorAdapter extends RecyclerView.Adapter<SelectorDataVHolder> {

        @Override public int getItemCount() { return structureData.size(); }

        @Override
        public SelectorDataVHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
            LayoutInflater li = LayoutInflater.from( getActivity() );
            return new SelectorDataVHolder( li, parent );
        }

        @Override
        public void onBindViewHolder(SelectorDataVHolder vh, int index) {
            vh.bind( structureData.get(index) );
        }
    }
}
