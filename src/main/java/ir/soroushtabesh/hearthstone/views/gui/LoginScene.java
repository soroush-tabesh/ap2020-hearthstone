package ir.soroushtabesh.hearthstone.views.gui;

import ir.soroushtabesh.hearthstone.util.FXUtil;
import ir.soroushtabesh.hearthstone.util.Messages;

public class LoginScene extends AbstractScene {
    public void showError() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , ""
                , Messages.LOGIN_PAGE_DIALOG_ERROR);
    }

    public void showExists() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_SIGN_UP
                , Messages.LOGIN_PAGE_DIALOG_EXISTS);
    }

    public void showWrong() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_LOGIN
                , Messages.LOGIN_PAGE_DIALOG_WRONG);
    }

    public void showCheckInput() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , ""
                , Messages.LOGIN_PAGE_DIALOG_CHECK_INPUT);
    }

    public void showSignUpSuccess() {
        FXUtil.showAlertInfo(Messages.LOGIN_PAGE_DIALOG_TITLE
                , Messages.LOGIN_PAGE_HEADER_SIGN_UP
                , Messages.LOGIN_PAGE_DIALOG_SUCCESS);
    }
}
