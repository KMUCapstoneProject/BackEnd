package kr.ac.kmu.Capstone.config.common.response;

public enum SuccessCode {

	USER_REGISTER_SUCCESS("REGISTER SUCCESS"),
	USER_SIGN_UP_SUCCESS("SIGN UP SUCCESS"),
	USER_LOGIN_SUCCESS("LOGIN SUCCESS"),
	USER_LOGOUT_SUCCESS("LOGOUT SUCCESS"),
	USER_FINDALL_SUCCESS("FIND ALL MEMBER Success"),
	USER_FINDMEMBER_SUCCESS("FIND USER INFO SUCCESS"),
	USER_UPDATE_PASSWORD_SUCESS("UPDATE PASSWORD SUCCESS"),
	USER_UPDATE_NICKNAME_SUCCESS("UPDATE NICKNAME SUCCESS"),
	USER_DELETE_SUCCESS("DELETE USER INFO SUCCESS"),
	USER_REFRESH_SUCCESS("ISSUE REFRESH TOKEN SUCCESS"),
	NICKNAME_REGISTER_POSSIBLE("NICKNAME_REGISTER_POSSIBLE"),
	NICKNAME_ALREADY_EXIST("NICKNAME_ALREADY_EXIST"),
	USER_ID_REGISTER_POSSIBLE("USER_ID_REGISTER_POSSIBLE"),
	USER_ID_ALREADY_EXIST("USER_ID_ALREADY_EXIST"),
	USER_PROFILE_UPLOAD_SUCCESS("USER PROFILE UPLOAD SUCCESS"),
	USER_RESAVE_DEVICE_TOKEN_SUCCESS("USER RE SAVE DEVICE TOKEN SUCCESS"),
	LIKE_BOARD_SAVE_SUCCESS("LIKE BOARD SAVE SUCCESS"),
	LIKE_BOARD_CANCLE_SUCCESS("LIKE BOARD CANCLE SUCCESS"),
	FIND_LIKE_BOARD_LIST_SUCCESS("FIND LIKE BOARD LIST SUCCESS"),
	LIKE_COMMENT_SAVE_SUCCESS("LIKE COMMENT SAVE SUCCESS"),
	LIKE_COMMENT_CANCLE_SUCCESS("LIKE COMMENT CANCLE SUCCESS"),
	FIND_LIKE_COMMENT_LIST_SUCCESS("FIND LIKE COMMENT LIST SUCCESS"),
	CHECK_LIKE_COMMENT_SUCCESS("CHECK LIKE COMMENT SUCCESS"),
	CHECK_LIKE_BOARD_SUCCESS("CHECK LIKE COMMENT SUCCESS")

	;

	private final String message;

	SuccessCode(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}