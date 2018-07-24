package com.udacity.surbi.listnow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private Unbinder unbinder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        rvList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLayoutManager);
        List<ItemList> myDataset = new ArrayList<>();
        myDataset.add(new ItemList("Clothes Shopping", true));
        myDataset.add(new ItemList("Christmas presents", true));
        myDataset.add(new ItemList("Dads party", true));
        myDataset.add(new ItemList("Home shopping", true));
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
        Intent intent = new Intent(getContext(), CheckListContainerActivity.class);
        startActivity(intent);
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

    }

    private void onShareMenuClicked(ItemList itemList) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, itemList.getTitle());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void onRenameMenuClicked(ItemList itemList) {

    }

    private void onCompletedMenuClicked(ItemList itemList) {

    }

    private void onFavoriteMenuClicked(ItemList itemList) {

    }

    private void onDeleteMenuClicked(ItemList itemList) {

    }
}
