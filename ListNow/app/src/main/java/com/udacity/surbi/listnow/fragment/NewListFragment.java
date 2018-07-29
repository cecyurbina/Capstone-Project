package com.udacity.surbi.listnow.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

import static android.app.Activity.RESULT_OK;
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
    private static final int PICK_IMAGE_REQUEST = 222;

    ValueEventListener mValueEventListener;
    DatabaseReference mDatabaseReference;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private Unbinder unbinder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Item> myDataset = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String key;
    private String currentKeyItemImage;
    private Bitmap currentBitmap;

    DatabaseHelper databaseHelper = new DatabaseHelper();
    private Uri filePath;

    private OnFragmentInteractionListener mListener;
    private StorageReference storageReference;
    private boolean isTaskRunning = false;
    private ProgressDialog progressDialog;

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
                    listStructure = databaseHelper.createList();
                    key = listStructure.getId();
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
        databaseHelper = new DatabaseHelper();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("lists").child(key).child("items");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

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

        getActivity().setTitle(listStructure.getName());
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
        updateProgressBar();
    }

    @Override
    public void selectImage(Item item) {
        currentKeyItemImage = item.getKey();
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);
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
            if (requestCode == CODE_RESULT_NEW) {
                String result = data.getStringExtra(KEY_RESULT_ITEM);
                ObjectMapper mapper = new ObjectMapper();
                Item obj = new Item();
                try {
                    obj = mapper.readValue(result, Item.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resultCode == RESULT_OK) {
                    myDataset.add(obj);
                    showData();
                    mAdapter.notifyDataSetChanged();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            } else if (requestCode == CODE_RESULT_EDIT) {
                String result = data.getStringExtra(KEY_RESULT_ITEM);
                ObjectMapper mapper = new ObjectMapper();
                Item obj = new Item();
                try {
                    obj = mapper.readValue(result, Item.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (resultCode == RESULT_OK) {
                    int positionToUpdate = findPositionById(obj.getKey());
                    if (positionToUpdate != error_not_found) {
                        myDataset.set(positionToUpdate, obj);
                        mAdapter.notifyItemChanged(positionToUpdate);
                    }
                }

                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null){
                filePath = data.getData();
                uploadFile();
                try {
                    currentBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    int positionToUpdate = findPositionById(currentKeyItemImage);
                    if (positionToUpdate != error_not_found){
                        myDataset.get(positionToUpdate).setBitmap(currentBitmap);
                        mAdapter.notifyDataSetChanged();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
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

    @Override
    public void onStart() {
        super.onStart();

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myDataset.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Item item = new Item();
                    item.setKey(childDataSnapshot.getKey());
                    item.setName((String) childDataSnapshot.child("name").getValue());
                    item.setImage((Boolean) childDataSnapshot.child("image").getValue());
                    item.setUnit((String) childDataSnapshot.child("unit").getValue());
                    if (childDataSnapshot.child("quantity").getValue() != null) {
                        item.setQuantity(((Long) childDataSnapshot.child("quantity").getValue()).intValue());
                    }
                    item.setImageUrl((String) childDataSnapshot.child("imageUrl").getValue());
                    item.setRejected((Boolean) childDataSnapshot.child("rejected").getValue());
                    item.setChecked((Boolean) childDataSnapshot.child("checked").getValue());
                    if (currentKeyItemImage != null){
                        if (item.getKey().endsWith(currentKeyItemImage)){
                            item.setBitmap(currentBitmap);
                        }
                    }

                    myDataset.add(item);
                }
                mAdapter.notifyDataSetChanged();
                updateProgressBar();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addValueEventListener(valueEventListener);
        mValueEventListener = valueEventListener;

    }

    private void updateProgressBar() {
        int count = 0;
        for (Item item: myDataset){
            if (item.isChecked()){
                count ++;
            }
        }
        int totalChecks=myDataset.size();
        double total = 0;
        if (totalChecks != 0) {
            total =  (((double) count / (double) totalChecks) * 100);
        }
       progressBar.setProgress((int)Math.ceil(total));



    }

    @Override
    public void onStop() {
        super.onStop();
        if (mValueEventListener != null){
            mDatabaseReference.removeEventListener(mValueEventListener);
        }

    }

    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            int index = filePath.toString().lastIndexOf("\\");
            String fileName = filePath.toString().substring(index + 1);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference riversRef = storageReference.child("images/"+fileName);
            UploadTask uploadTask = riversRef.putFile(filePath);
            isTaskRunning = true;

            uploadTask
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            isTaskRunning = false;
                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            isTaskRunning = false;
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });


            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        int position = findPositionById(currentKeyItemImage);
                        if (position != error_not_found) {
                            Item item = myDataset.get(position);
                            item.setImageUrl(downloadUri.toString());
                            databaseHelper.setImageItem(key, item);
                            currentBitmap = null;
                            currentKeyItemImage = null;
                        }


                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });



        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isTaskRunning) {
            progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait a moment!");
        }
    }

}
