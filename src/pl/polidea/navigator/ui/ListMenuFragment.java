package pl.polidea.navigator.ui;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.ListMenu;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * List displaying fragment.
 */
public class ListMenuFragment extends AbstractMenuNavigatorFragment {

    private class ListMenuAdapter extends BaseAdapter {

        private final ListMenu listMenu;
        private final LayoutInflater inflater;

        public ListMenuAdapter(final ListMenu listMenu, final LayoutInflater inflater) {
            super();
            this.listMenu = listMenu;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return listMenu.items.length;
        }

        @Override
        public AbstractNavigationMenu getItem(final int position) {
            return listMenu.items[position];
        }

        @Override
        public long getItemId(final int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View listItemView = inflater.inflate(R.layout.single_list_item_layout, null);
            final AbstractNavigationMenu menu = getItem(position);
            final ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
            final TextView textView = (TextView) listItemView.findViewById(R.id.list_item_text);
            if (menu.iconFile == null) {
                imageView.setVisibility(View.GONE);
            } else {
                final Bitmap bitmap = bitmapReader.getBitmap(menu.iconFile);
                if (bitmap == null) {
                    imageView.setVisibility(View.GONE);
                } else {
                    if (menu.isDisabled()) {
                        final Bitmap bitmapGray = bitmapReader.getGrayBitmap(menu.iconFile);
                        imageView.setImageBitmap(bitmapGray);
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
            textView.setText(menu.name);
            if (menu.isDisabled()) {
                textView.setTextColor(Color.GRAY);
            }
            if (!menu.isDisabled()) {
                listItemView.setOnClickListener(new MenuNavigatorOnClickListener(menu));
            }
            return listItemView;
        }
    }

    private class LatestListAdapter extends BaseAdapter {

        private final TextView latestText;
        // first = description, second = transaction
        private List<Pair<String, String>> dTList;
        private final LayoutInflater inflater;

        /**
         * @param descriptionTransactionPairsList
         *            first = description, second = transaction
         */
        public LatestListAdapter(final List<Pair<String, String>> descriptionTransactionPairsList,
                final TextView latestText, final LayoutInflater inflater) {
            this.dTList = descriptionTransactionPairsList;
            this.inflater = inflater;
            if (dTList == null) {
                dTList = new ArrayList<Pair<String, String>>();
            }

            this.latestText = latestText;
            if (dTList.isEmpty()) {
                latestText.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCount() {
            return dTList.size();
        }

        @Override
        public Object getItem(final int position) {
            return dTList.get(position);
        }

        @Override
        public long getItemId(final int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View listItemView = inflater.inflate(R.layout.single_list_item_layout, null);
            final ImageView imageView = (ImageView) listItemView.findViewById(R.id.list_item_image);
            final TextView textView = (TextView) listItemView.findViewById(R.id.list_item_text);
            imageView.setVisibility(View.GONE);
            textView.setText(dTList.get(position).first);
            return listItemView;
        }
    }

    @Override
    public ListMenu getNavigationMenu() {
        return (ListMenu) super.getNavigationMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup listViewGroup = (ViewGroup) inflater.inflate(R.layout.list_fragment_layout, container, false);
        final ListView listView = (ListView) listViewGroup.findViewById(R.id.listView);
        final TextView latestText = (TextView) listViewGroup.findViewById(R.id.latestChoosenTextView);
        final ListView latestChoosenListView = (ListView) listViewGroup.findViewById(R.id.latestChoosenListView);
        listView.setAdapter(new ListMenuAdapter(getNavigationMenu(), inflater));
        latestChoosenListView.setAdapter(new LatestListAdapter(null, latestText, inflater));

        return listViewGroup;
    }
}
