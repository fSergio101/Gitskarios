package com.alorma.github.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.CommitFile;
import com.alorma.github.sdk.bean.info.CommitInfo;
import com.alorma.github.ui.activity.base.BackActivity;
import com.alorma.github.ui.adapter.commit.CommitFilesAdapter;
import com.alorma.github.ui.fragment.FileFragment;
import com.alorma.github.ui.fragment.commit.SingleCommitFragment;

/**
 * Created by Bernat on 22/12/2014.
 */
public class CommitDetailActivity extends BackActivity implements CommitFilesAdapter.OnFileRequestListener {

	private boolean tablet;

	public static Intent launchIntent(Context context, CommitInfo commitInfo) {
		Bundle b = new Bundle();
		b.putParcelable(SingleCommitFragment.INFO, commitInfo);

		Intent intent = new Intent(context, CommitDetailActivity.class);
		intent.putExtras(b);

		return intent;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commit_activity);

		if (getIntent().getExtras() != null) {
			CommitInfo info = getIntent().getExtras().getParcelable(SingleCommitFragment.INFO);

			setTitle(getString(R.string.title_activity_commit_detail, info.sha));

			if (getSupportActionBar() != null) {
				getSupportActionBar().setSubtitle(String.valueOf(info.repoInfo));
			}

			tablet = findViewById(R.id.detail) != null;

			SingleCommitFragment singleCommitFragment = SingleCommitFragment.newInstance(info);
			singleCommitFragment.setOnFileRequestListener(this);

			FileFragment fileFragment = new FileFragment();

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.content, singleCommitFragment);
			if (tablet) {
				ft.replace(R.id.detail, fileFragment);
			}
			ft.commit();

		}
	}

	@Override
	public void onFileRequest(CommitFile file) {
		if (tablet) {
			FileFragment fileFragment = new FileFragment();

			Bundle bundle = new Bundle();
			bundle.putString(FileFragment.PATCH, file.patch);
			bundle.putString(FileFragment.NAME, file.getFileName());

			fileFragment.setArguments(bundle);

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.detail, fileFragment);
			ft.commit();
		} else {
			Intent launcherIntent = FileActivity.createLauncherIntent(this, file.patch, file.getFileName());
			startActivity(launcherIntent);
		}
	}

	@Override
	public boolean openFirstFile() {
		return tablet;
	}
}
