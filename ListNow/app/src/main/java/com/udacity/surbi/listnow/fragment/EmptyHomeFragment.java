package com.udacity.surbi.listnow.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.udacity.surbi.listnow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EmptyHomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EmptyHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmptyHomeFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.button_new_list)
    Button button_new;
    @BindView(R.id.button_search)
    Button button_search;

    private OnFragmentInteractionListener mListener;

    public EmptyHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create mValueEventListener new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EmptyHomeFragment.
     */
    public static EmptyHomeFragment newInstance() {
        EmptyHomeFragment fragment = new EmptyHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empty_home, container, false);
        unbinder = ButterKnife.bind(this, view);
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
        void newList();

        void searchList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.button_new_list)
    public void newList(View view) {
        mListener.newList();
    }

    @OnClick(R.id.button_search)
    public void search(View view) {
        mListener.searchList();
    }
}
