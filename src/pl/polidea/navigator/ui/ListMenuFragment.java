package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.ListMenu;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
        listView.setAdapter(new ListMenuAdapter(getNavigationMenu(), inflater));
        return listViewGroup;
    }
}
