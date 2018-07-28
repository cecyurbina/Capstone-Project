package com.udacity.surbi.listnow.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.activity.NewItemActivity;
import com.udacity.surbi.listnow.adapter.CheckListAdapter;
import com.udacity.surbi.listnow.adapter.PreviewListListener;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ListStructure;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewListFragment extends Fragment implements PreviewListListener {
    public static final String KEY_LIST_ID = "key_list_id";
    public static final String KEY_ITEM = "key_item";
    public static final String KEY_EDITION = "key_edition";


    public static final int CODE_RESULT_NEW = 100;
    public static final int CODE_RESULT_EDIT = 101;
    public static final String KEY_RESULT_ITEM = "key_result_item";
    private static final int error_not_found = -1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListStructure listStructure;
    @BindView(R.id.tv_empty_message)
    TextView tvMessage;
    @BindView(R.id.rv_structure_list)
    RecyclerView rvList;
    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    private Unbinder unbinder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Item> myDataset = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String key;
    DatabaseHelper databaseHelper = new DatabaseHelper();

    private OnFragmentInteractionListener mListener;

    public NewListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create mValueEventListener new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewListFragment newInstance(String param1, String param2) {
        NewListFragment fragment = new NewListFragment();
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
        //if (savedInstanceState == null) {
        //    DatabaseHelper databaseHelper = new DatabaseHelper();
        //} else {
        //    key = savedInstanceState.getString(KEY_LIST_ID);
        //}
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (mListener != null) {
                if (mListener.getList() == null) {
                    key = databaseHelper.createList();
                } else {
                    listStructure = mapper.readValue(mListener.getList(), ListStructure.class);
                    key = listStructure.getId();
                    myDataset = listStructure.getItems();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        mLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.addItemDecoration(new DividerItemDecoration(rvList.getContext(), DividerItemDecoration.VERTICAL));

        mAdapter = new CheckListAdapter(myDataset, this);
        rvList.setAdapter(mAdapter);

        showData();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewItemActivity.class);
                intent.putExtra(KEY_LIST_ID, key);
                intent.putExtra(KEY_EDITION, false);
                startActivityForResult(intent, CODE_RESULT_NEW);
            }
        });


        return view;
    }

    private void showData() {
        if (myDataset.size() > 0) {

            tvMessage.setVisibility(GONE);
            rvList.setVisibility(View.VISIBLE);
            rvList.setHasFixedSize(true);

        } else {
            tvMessage.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }
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
    public void onSelectedItem(final Item itemList, View view) {
        if (getContext() == null) {
            return;
        }
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.check_list_actions);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_edit:
                        onListEditClicked(itemList);
                        break;
                    case R.id.list_reject:
                        onListRejectClicked(itemList);
                        break;
                    case R.id.list_delete:
                        onListDeleteClicked(itemList);
                        break;

                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onCheck(Item item) {
        databaseHelper.checkItem(key, item);
    }

    private void onListRejectClicked(Item item) {
        item.setRejected(true);
        databaseHelper.rejectItemFromList(key, item.getKey());
        mAdapter.notifyDataSetChanged();
    }

    private void onListEditClicked(Item item) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getContext(), NewItemActivity.class);
        intent.putExtra(KEY_LIST_ID, key);
        intent.putExtra(KEY_EDITION, true);
        intent.putExtra(KEY_ITEM, jsonInString);
        startActivityForResult(intent, CODE_RESULT_EDIT);
    }

    private void onListDeleteClicked(Item item) {
        databaseHelper.removeItemFromList(key, item.getKey());
        int positionToDelete = findPositionById(item.getKey());
        if (positionToDelete != error_not_found) {
            myDataset.remove(positionToDelete);
            mAdapter.notifyItemRemoved(positionToDelete);
            mAdapter.notifyItemRangeChanged(positionToDelete, myDataset.size());
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

        String getList();

        boolean isNewList();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_LIST_ID, key);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String result = data.getStringExtra(KEY_RESULT_ITEM);
            ObjectMapper mapper = new ObjectMapper();
            Item obj = new Item();
            try {
                obj = mapper.readValue(result, Item.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (requestCode == CODE_RESULT_NEW) {
                if (resultCode == Activity.RESULT_OK) {
                    myDataset.add(obj);
                    showData();
                    mAdapter.notifyDataSetChanged();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            } else if (requestCode == CODE_RESULT_EDIT) {
                if (resultCode == Activity.RESULT_OK) {
                    int positionToUpdate = findPositionById(obj.getKey());
                    if (positionToUpdate != error_not_found) {
                        myDataset.set(positionToUpdate, obj);
                        mAdapter.notifyItemChanged(positionToUpdate);
                    }
                }

                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
        }

    }//onActivityResult


    /**
     * find element position by id
     *
     * @param id element to find
     * @return position
     */
    private int findPositionById(String id) {
        for (int i = 0; i < myDataset.size(); i++) {
            if (myDataset.get(i).getKey().equals(id)) {
                return i;
            }
        }
        return error_not_found;
    }
}
