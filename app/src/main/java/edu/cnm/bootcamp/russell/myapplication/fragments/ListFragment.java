package edu.cnm.bootcamp.russell.myapplication.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import edu.cnm.bootcamp.russell.myapplication.R;
import edu.cnm.bootcamp.russell.myapplication.adapters.ImageCursorAdapter;
import edu.cnm.bootcamp.russell.myapplication.api.APIMethods;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment implements AbsListView.OnScrollListener {
    private static final String ARG_SUBREDDIT = "subreddit";
    private String mSubreddit;
    private ImageCursorAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    private ListView mListView;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    public static ListFragment newInstance(String subreddit) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBREDDIT, subreddit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mSubreddit = args.getString(ARG_SUBREDDIT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mListView = (ListView)view.findViewById(R.id.fragmentList);
        mListView.setOnScrollListener(this);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadImages();
        getImages();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.close();
        }
        super.onDestroy();
    }

    public void setSubreddit(String subreddit) {
        mSubreddit = subreddit;
        getImages();
    }

    private void getImages() {
        APIMethods.getSubredditGallery(getActivity(), mSubreddit, new Runnable() {
            @Override
            public void run() {
                loadImages();
            }
        });
    }

    private void loadImages() {
        if (mAdapter == null) {
            mAdapter = new ImageCursorAdapter(getContext(), mSubreddit);
            mListView.setAdapter(mAdapter);
        }
        else {
            mAdapter.setSubreddit(mSubreddit);
        }

        Activity activity = getActivity();
        if (activity != null) {
            activity.setTitle("/r/" + mSubreddit);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mAdapter != null) {
            mAdapter.setFlingMode(scrollState == SCROLL_STATE_FLING);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
        void onCloseClicked();
    }
}
