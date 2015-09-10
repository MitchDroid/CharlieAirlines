package com.southwest.southwestapp.fragments;

import com.southwest.southwestapp.AppHelper;
import com.southwest.southwestapp.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by emiliano.gudino on 07/09/2015.
 */
public class BoardingPassFragment extends BaseFragment implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boarding_pass, container, false);

        mToolbar = (Toolbar)view.findViewById(R.id.toolbar);
        setUpToolBar();

        mToolbar.setOnMenuItemClickListener(this);

        mToolbar.inflateMenu(R.menu.menu_boarding_pass);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUpToolBar() {
        mToolbar.setTitle(getResources().getString(R.string.boarding_pass_title));
        mToolbar.setSubtitle(getResources().getString(R.string.boarding_pass_subtitle));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.neutral_white));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.primary_blue));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionCancel:
                AppHelper.screenManager.showCheckInSearchScreen(getActivity());
                return true;
            case R.id.optionSend:
                return true;
            case R.id.optionSave:
                showAlertSaveToPhotos();
                return true;
            default:
                return false;
        }
    }

    private void showAlertSaveToPhotos() {

        AppHelper.dialogManager.showDialog(getActivity(),
                                           getResources().getString(R.string.boarding_pass_save_dialog_title),
                                           getResources().getString(R.string.boarding_pass_save_dialog_message),
                                           getResources().getString(R.string.boarding_pass_save_dialog_yes),
                                           new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   AppHelper.screenManager.showMainScreen(getActivity());
                                               }
                                           },
                                           getResources().getString(R.string.boarding_pass_save_dialog_no),
                                           new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialog, int which) {
                                                   //Complete flow
                                               }
                                           }, false);
    }
}