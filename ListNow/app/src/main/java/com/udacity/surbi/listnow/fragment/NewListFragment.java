package com.udacity.surbi.listnow.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.activity.NewItemActivity;
import com.udacity.surbi.listnow.adapter.CheckListAdapter;
import com.udacity.surbi.listnow.adapter.PreviewListListener;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ItemList;
import com.udacity.surbi.listnow.data.ListStructure;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

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
public class NewListFragment extends Fragment implements PreviewListListener{
    public static final String KEY_LIST_ID = "key_list_id";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListStructure listStructure ;
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

    private OnFragmentInteractionListener mListener;

    public NewListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
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
        //setRetainInstance(true);
        if (savedInstanceState == null) {
            DatabaseHelper databaseHelper = new DatabaseHelper();
            key = databaseHelper.createList();
        } else {
            key = savedInstanceState.getString(KEY_LIST_ID);
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_new_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (true){
            listStructure = getJsonList();
            myDataset = listStructure.getItems();
        }
        if (myDataset.size() > 0){
            tvMessage.setVisibility(GONE);
            rvList.setVisibility(View.VISIBLE);
            rvList.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            rvList.setLayoutManager(mLayoutManager);
            rvList.addItemDecoration(new DividerItemDecoration(rvList.getContext(), DividerItemDecoration.VERTICAL));

            mAdapter = new CheckListAdapter(myDataset, this);
            rvList.setAdapter(mAdapter);
        } else {
            tvMessage.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getContext(), NewItemActivity.class);
            intent.putExtra(KEY_LIST_ID, key);
            startActivity(intent);
            }
        });
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
    public void onSelectedItem(Item item, View view) {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private ListStructure getJsonList()  {
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

        return listStructure;
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            key = savedInstanceState.getString(KEY_LIST_ID);
        }
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_LIST_ID, key);
    }


}
