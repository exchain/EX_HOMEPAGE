package com.finger.fwf.uxui.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgManager;
import com.finger.agent.msg.MsgObj;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.MarketLoad;
import com.finger.fwf.uxui.util.MarketUser;
import com.finger.fwf.uxui.util.PrevLoadUX;
import com.finger.fwf.uxui.util.ResponseUX;
import com.finger.fwf.uxui.util.UXUtil;

public class UXMarketService extends BasisService {
    private static Logger logger = Logger.getLogger(UXMarketService.class);

    private static UXMarketService _instance = new UXMarketService();

    protected UXMarketService(){
        logger.debug(":: "+ this.getClass() +" ~~~~~~~~~~~~~~~~~~~~~");
    }

    public static UXMarketService getInstance() {
        logger.debug(":: getInstance() ~~~~~~~~~~~~~~~~~~~~~");
        return _instance;
    }

    /**
     * 요청정보를 서버에 전송하고 결과를 리턴받아 오는 서비스
     * @param request
     * @param response
     * @throws IOException 
     * @throws ServletException 
     */
    public void doUXService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug(":: doUXService() ~~~~~~~~~~~~~~~~~~~~~");

        HttpServletUX.setResponseContentAndHeader( response );

        MsgObj _msg_ = null;
        try {
            if (getParametersSize(request, logger) > 0) {

                String task_id = getReqTaskId(request);	//request.getParameter("_TASK_ID_");

                if ( !UXUtil.isNull(task_id) )
                {
                    if ("MarketReload".equals(task_id))
                    {
                        PrevLoadUX.previousLoading(request.getParameter(_Specify_Task));
                    } else
                    if ("SystemInfo".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketLoad.getSystemInfo(request) );
                    } else
                    if ("CrpnInfo".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketLoad.getCrpnInfo(request) );
                    } else
                    if ("BizrInfo".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketLoad.getBizrInfo(request) );
                    } else
                    if ("CommonCode".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketLoad.getCommonCode(request) );
                    } else
                    if ("Select5000".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketLoad.getSelectbox5000(request) );
                    }
                    else
                    if ("UserPwdCheck".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketUser.checkUserPassword(request) );
                    } else
                    if ("UserCrpnInfo".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketUser.getUserCrpnInfo(request) );
                    } else
                    if ("UserBizrInfo".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketUser.getUserBizrInfo(request) );
                    } else
                    if ("UserSelect5050".equals(task_id)) {
                        ResponseUX.toGsonByMarketResult(response, MarketUser.getUserSelectbox5050(request) );
                    }
                }	//if ( !UXUtil.isNull(task_id) )
                else {
                    throw new AgentException(new MsgObj(FixedMsg._MSG_TASK_T0000, "Please contact the system administrator."));
                }
            }
            else {
                throw new AgentException(new MsgObj(FixedMsg._MSG_TASK_T0002, "Please contact the system administrator."));
            }
        } catch (AgentException ee) {
            ee.printStackTrace();
            _msg_ = MsgManager.getInstance().createMessage(ee);
        } finally {
            ResponseUX.toGsonByActionError( request, response, _msg_ );
        }
    }
    protected static final String _Specify_Task = "specifyTask";
}