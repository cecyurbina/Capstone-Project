package com.udacity.surbi.listnow.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.surbi.listnow.activity.CheckListContainerActivity;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.adapter.ListAdapter;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ItemList;
import com.udacity.surbi.listnow.data.ListStructure;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListHomeFragment extends Fragment implements ListAdapter.OnItemSelectedListener {
    public static final String KEY_LIST_JSON = "key_list_json";
    private static final int error_not_found = -1;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private Unbinder unbinder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ItemList> myDataset = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListHomeFragment newInstance(String param1, String param2) {
        ListHomeFragment fragment = new ListHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        myDataset.add(new ItemList(1, "Clothes Shopping", false, false));
        myDataset.add(new ItemList(2, "Christmas presents", false, false));
        myDataset.add(new ItemList(3, "Dads party", false, false));
        myDataset.add(new ItemList(4, "Home shopping", false, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        rvList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLayoutManager);

        mAdapter = new ListAdapter(myDataset, this);
        rvList.setAdapter(mAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSelectedItem(ItemList itemList) {
        try {
            String jsonList = getJsonList();
            Intent intent = new Intent(getContext(), CheckListContainerActivity.class);
            intent.putExtra(KEY_LIST_JSON, jsonList);
            startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSelectedItemMenu(final ItemList itemList, View view) {
        if (getContext() == null) {
            return;
        }
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.activity_main_list_actions);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_empty_copy:
                        onCopyMenuClicked(itemList, true);
                        break;
                    case R.id.list_copy:
                        onCopyMenuClicked(itemList, false);
                        break;
                    case R.id.list_share:
                        onShareMenuClicked(itemList);
                        break;
                    case R.id.list_rename:
                        onRenameMenuClicked(itemList);
                        break;
                    case R.id.list_completed:
                        onCompletedMenuClicked(itemList);
                        break;
                    case R.id.list_favorite:
                        onFavoriteMenuClicked(itemList);
                        break;
                    case R.id.list_delete:
                        onDeleteMenuClicked(itemList);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onCopyMenuClicked(ItemList itemList, boolean isEmpty) {
        ItemList newItemListCopied = new ItemList(myDataset.size() + 2, itemList.getTitle() + " " + getString(R.string.home_list_copy_added_to_name), false, false);
        myDataset.add(newItemListCopied);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Open apps to share text
     *
     * @param itemList item to share
     */
    private void onShareMenuClicked(ItemList itemList) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, itemList.getTitle());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void onRenameMenuClicked(ItemList itemList) {
        showInputDialog(itemList);
    }

    private void onCompletedMenuClicked(ItemList itemList) {
        int position = findPositionById(itemList.getId());
        if (position != error_not_found) {
            ItemList item = myDataset.get(position);
            myDataset.get(position).setCompleted(!item.isCompleted());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onFavoriteMenuClicked(ItemList itemList) {
        int position = findPositionById(itemList.getId());
        if (position != error_not_found) {
            ItemList item = myDataset.get(position);
            myDataset.get(position).setFavorite(!item.isFavorite());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onDeleteMenuClicked(ItemList itemList) {
        int position = findPositionById(itemList.getId());
        if (position != error_not_found) {
            myDataset.remove(position);
            mAdapter.notifyItemRemoved(position);
            mAdapter.notifyItemRangeChanged(position, myDataset.size());
        }
    }

    /**
     * Show dialog to enter new name
     *
     * @param itemList item to modify
     */
    private void showInputDialog(final ItemList itemList) {
        if (getContext() != null) {
            final Button buttonAccept;
            final EditText etNewName = new EditText(getContext());
            etNewName.setHint(getString(R.string.home_dialog_rename_new_name));

            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()).setTitle(getString(R.string.home_dialog_rename_title, itemList.getTitle())).setView(etNewName).setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    itemList.setTitle(etNewName.getText().toString());
                    updateListNewTitle(itemList);
                }
            }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            buttonAccept = dialog.show().getButton(AlertDialog.BUTTON_POSITIVE);
            //disable accept if input is empty
            buttonAccept.setEnabled(false);

            //validate empty list name
            etNewName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //enable button if input is not empty
                    if (etNewName.getText().length() > 0) {
                        buttonAccept.setEnabled(true);
                    } else {
                        buttonAccept.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    /**
     * Update list in view after list item is renamed
     *
     * @param itemList item modified
     */
    private void updateListNewTitle(ItemList itemList) {
        for (ItemList il : myDataset) {
            if (il.getId() == itemList.getId()) {
                il.setTitle(itemList.getTitle());
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * find element position by id
     *
     * @param id element to find
     * @return position
     */
    private int findPositionById(int id) {
        int positionToRemove;
        for (int i = 0; i < myDataset.size(); i++) {
            if (myDataset.get(i).getId() == id) {
                return i;
            }
        }
        return error_not_found;
    }

    private String getJsonList() throws JsonProcessingException {
        ListStructure listStructure = new ListStructure();
        listStructure.setId(1);
        listStructure.setCompleted(false);
        listStructure.setFavorite(false);

        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setImage(false);
        item1.setName("Shampoo");
        item1.setQuantity(2);
        item1.setUnit("botes");
        item1.setRejected(true);

        Item item2 = new Item();
        item2.setImage(true);
        item2.setName("Jabon");
        item2.setQuantity(2);
        item2.setUnit("barras");
        item2.setRejected(true);

        Item item3 = new Item();
        item3.setImage(true);
        item3.setName("Pasta dental");
        item3.setRejected(false);

        items.add(item1);
        items.add(item2);
        items.add(item3);

        listStructure.setItems(items);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(listStructure);
        return jsonInString;
    }
}
