package com.finger.fwf.core.task;


public class Work2Log {
	//private static final Logger	logger	= Logger.getLogger(Work2Log.class);

	public Work2Log() {
	}

	/**
	 * DB테이블(FWF_SYS_JOBLOG)에 에러(로그)정보를 기록한다.
	 * @param type_cd
	 * @param task_id
	 * @param wrk_user_id
	 * @param err_msg
	 * @param err_eft
	 * @param err_dps
	 */
	private static void log(String type_cd, String task_id, String wrk_user_id, String err_msg, String err_eft, String err_dps) {

		/*
		ThrowTraceInfo li = new ThrowTraceInfo(new Throwable(),2);
		String rs_cls_nm = li.getClassName();
		String rs_mth_nm = li.getMethodName();

		if (wrk_user_id.length() > 20) {
			wrk_user_id = wrk_user_id.substring(0, 8);
		}

		logger.debug("---------------------------------------------");
		logger.debug("| type_cd : " + type_cd );
		logger.debug("| task_id : " + task_id );
		logger.debug("| wrk_user_id : " + wrk_user_id );
		logger.debug("| err_msg : " + err_msg );
		logger.debug("| err_eft : " + err_eft );
		logger.debug("| err_dps : " + err_dps );
		logger.debug("| rs_cls_nm : " + rs_cls_nm );
		logger.debug("| rs_mth_nm : " + rs_mth_nm );
		logger.debug("---------------------------------------------");

		FwtpMessage fwtpMsg = new FwtpMessage();
		@SuppressWarnings("unused")
		FwtpMessage ret = null;
		try {
			StringBuilder sb = new StringBuilder();
			fwtpMsg.setTaskId("Task2LogI");
			sb.append("type_cd	task_id	wrk_user_id	err_msg	err_eft	err_dps	rs_cls_nm	rs_mth_nm\n");
			sb.append(type_cd);
			sb.append("\t");
			sb.append(task_id);
			sb.append("\t");
			sb.append(wrk_user_id);
			sb.append("\t");
			sb.append(err_msg.replace('\t', ' '));
			sb.append("\t");
			sb.append(err_eft.replace('\t', ' '));
			sb.append("\t");
			sb.append(err_dps.replace('\t', ' '));
			sb.append("\t");
			sb.append(rs_cls_nm);
			sb.append("\t");
			sb.append(rs_mth_nm);

			fwtpMsg.setRequestData(sb.toString());
			logger.debug( sb.toString() );
			ret = FwfTasksLoader.loadTransTask(fwtpMsg);
		}
		catch (Exception e) {
			logger.error("LOG(DB) 작성에 실패하였습니다!");
			logger.error(e.getMessage(), e);
		}
		*/
	}

	private static final String C_TYPE_I = "I";
	private static final String C_TYPE_E = "E";
	private static final String C_USER_ID = "FWFAPSYS";

	// info
	public static void info(String err_msg) {
		log(C_TYPE_I, "", C_USER_ID, err_msg, "", "");
	}
	public static void info(String err_msg, String task_id) {
		log(C_TYPE_I, task_id, C_USER_ID, err_msg, "", "");
	}
	public static void info(String err_msg, String task_id, String wrk_user_id) {
		log(C_TYPE_I, task_id, wrk_user_id, err_msg, "", "");
	}

	// error
	public static void error(Exception e) {
		log(C_TYPE_E, "", C_USER_ID, e.getMessage(), "", "");
	}
	public static void error(String err_msg) {
		log(C_TYPE_E, "", C_USER_ID, err_msg, "", "");
	}
	public static void error(String err_msg, String task_id, String err_eft, String err_dps) {
		log(C_TYPE_E, task_id, C_USER_ID, err_msg, err_eft, err_dps);
	}
	public static void error(String err_msg, String task_id, String err_eft, String err_dps, String wrk_user_id) {
		log(C_TYPE_E, task_id, wrk_user_id, err_msg, err_eft, err_dps);
	}
}
