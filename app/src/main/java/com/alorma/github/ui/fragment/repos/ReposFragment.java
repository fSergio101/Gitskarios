package com.alorma.github.ui.fragment.repos;

import android.os.Bundle;
import android.widget.Toast;

import com.alorma.github.sdk.bean.dto.response.ListRepos;
import com.alorma.github.sdk.services.repos.BaseReposClient;
import com.alorma.github.sdk.services.repos.UserReposClient;

public class ReposFragment extends BaseReposListFragment {

    private String username;

    public static ReposFragment newInstance() {
        return new ReposFragment();
    }

    public static ReposFragment newInstance(String username) {
        ReposFragment reposFragment = new ReposFragment();
        if (username != null) {
            Bundle bundle = new Bundle();
            bundle.putString(USERNAME, username);

            reposFragment.setArguments(bundle);
        }
        return reposFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    protected void executeRequest() {
        BaseReposClient client;

        client = new UserReposClient(getActivity(), username);

        client.setOnResultCallback(this);
        client.execute();
    }

    @Override
    protected void executePaginatedRequest(int page) {
        UserReposClient client = new UserReposClient(getActivity(), username, page);
        client.setOnResultCallback(this);
        client.execute();
    }

    @Override
    protected void onResponse(ListRepos repos) {
        if (reposAdapter == null) {
            setUpList();
        }
        reposAdapter.addAll(repos);
    }

    @Override
    protected boolean useFab() {
        return false && username == null;
    }

    @Override
    protected void onFabClick() {
        Toast.makeText(getActivity(), "Hello FAB", Toast.LENGTH_SHORT).show();
    }
}
