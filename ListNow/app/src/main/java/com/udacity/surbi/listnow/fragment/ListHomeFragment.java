package com.udacity.surbi.listnow.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.activity.CheckListContainerActivity;
import com.udacity.surbi.listnow.adapter.ListAdapter;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ListStructure;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

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
public class ListHomeFragment extends Fragment implements ListAdapter.OnItemSelectedListener, RenameDialogFragment.RenameDialogListener {
    public static final String KEY_LIST_JSON = "key_list_json";
    private static final int error_not_found = -1;

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private Unbinder unbinder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ListStructure> myDataset = new ArrayList<>();
    DatabaseHelper mDatabaseHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ValueEventListener mValueEventListener;
    DatabaseReference mDatabaseReference;
    private OnFragmentInteractionListener mListener;

    public ListHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create mValueEventListener new instance of
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
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_home, container, false);
        mDatabaseHelper = new DatabaseHelper();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("lists");
        unbinder = ButterKnife.bind(this, view);

        rvList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLayoutManager);

        mAdapter = new ListAdapter(myDataset, this);
        rvList.setAdapter(mAdapter);

        return view;
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
    public void onSelectedItem(ListStructure itemList) {
        try {
            String jsonList = getJsonList(itemList);
            Intent intent = new Intent(getContext(), CheckListContainerActivity.class);
            intent.putExtra(KEY_LIST_JSON, jsonList);
            startActivity(intent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSelectedItemMenu(final ListStructure itemList, View view) {
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

    @Override
    public void onFinishRenameDialog(String inputText, String id) {
        int position = findPositionById(id);
        if (position != error_not_found) {
            mDatabaseHelper.renameList(id, inputText);
            ListStructure item = myDataset.get(position);
            item.setName(inputText);
            mAdapter.notifyDataSetChanged();

        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <mValueEventListener href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</mValueEventListener> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void onCopyMenuClicked(ListStructure itemList, boolean isEmpty) {
        setItemsOnList(itemList);
        myDataset.add(mDatabaseHelper.copyList(itemList));
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Open apps to share text
     *
     * @param itemList item to share
     */
    private void onShareMenuClicked(ListStructure itemList) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, itemList.getName());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void onRenameMenuClicked(ListStructure itemList) {
        showAlertDialog(itemList);
    }

    private void onCompletedMenuClicked(ListStructure itemList) {
        int position = findPositionById(itemList.getId());
        if (position != error_not_found) {
            ListStructure item = myDataset.get(position);
            mDatabaseHelper.completeList(itemList.getId(), !item.getCompleted());
            myDataset.get(position).setCompleted(!item.getCompleted());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onFavoriteMenuClicked(ListStructure itemList) {
        int position = findPositionById(itemList.getId());
        if (position != error_not_found) {
            ListStructure item = myDataset.get(position);
            mDatabaseHelper.favoriteList(itemList.getId(), !item.getFavorite());
            myDataset.get(position).setFavorite(!item.getFavorite());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void onDeleteMenuClicked(ListStructure itemList) {
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
    private void showInputDialog(final ListStructure itemList) {
        if (getContext() != null) {
            final Button buttonAccept;
            final EditText etNewName = new EditText(getContext());
            etNewName.setHint(getString(R.string.home_dialog_rename_new_name));

            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext()).setTitle(getString(R.string.home_dialog_rename_title, itemList.getName())).setView(etNewName).setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    itemList.setName(etNewName.getText().toString());
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
    private void updateListNewTitle(ListStructure itemList) {
        for (ListStructure il : myDataset) {
            if (il.getId().equals(itemList.getId())) {
                il.setName(itemList.getName());
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
    private int findPositionById(String id) {
        int positionToRemove;
        for (int i = 0; i < myDataset.size(); i++) {
            if (myDataset.get(i).getId().equals(id)) {
                return i;
            }
        }
        return error_not_found;
    }

    private String getJsonList(ListStructure listStructure) throws JsonProcessingException {
        setItemsOnList(listStructure);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(listStructure);
    }

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myDataset.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    ListStructure listStructure = new ListStructure();
                    listStructure.setId(childDataSnapshot.getKey());
                    listStructure.setName((String) childDataSnapshot.child("name").getValue());
                    listStructure.setCompleted((Boolean) childDataSnapshot.child("completed").getValue());
                    listStructure.setFavorite((Boolean) childDataSnapshot.child("favorite").getValue());
                    listStructure.setDataSnapshot(childDataSnapshot.child("items"));
                    myDataset.add(listStructure);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
        mValueEventListener = valueEventListener;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mValueEventListener != null){
            mDatabaseReference.removeEventListener(mValueEventListener);
        }

    }

    private void setItemsOnList(ListStructure listStructure){
        List<Item> items = new ArrayList<>();

        for (DataSnapshot childDataSnapshot : listStructure.getDataSnapshot().getChildren()) {
            Item item2 = new Item();
            item2.setKey(childDataSnapshot.getKey());
            item2.setName((String) childDataSnapshot.child("name").getValue());
            item2.setImage((Boolean) childDataSnapshot.child("image").getValue());
            if (childDataSnapshot.child("quantity").getValue() != null) {
                item2.setQuantity(((Long) childDataSnapshot.child("quantity").getValue()).intValue());
            }
            item2.setUnit((String) childDataSnapshot.child("unit").getValue());
            item2.setRejected((Boolean) childDataSnapshot.child("rejected").getValue());
            items.add(item2);
        }

        listStructure.setItems(items);
    }

    private void showAlertDialog(ListStructure listStructure) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        RenameDialogFragment alertDialog = RenameDialogFragment.newInstance(getString(R.string.home_dialog_rename_title, listStructure.getName()), listStructure.getId());
        alertDialog.setTargetFragment(ListHomeFragment.this, 200);
        alertDialog.show(fm, "fragment_alert");
    }

}
