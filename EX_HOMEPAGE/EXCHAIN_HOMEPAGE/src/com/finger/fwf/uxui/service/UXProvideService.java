package com.finger.fwf.uxui.service;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.finger.agent.config.FcAgentConfig;
import com.finger.agent.exception.AgentException;
import com.finger.agent.msg.FixedMsg;
import com.finger.agent.msg.MsgManager;
import com.finger.agent.msg.MsgObj;
import com.finger.fwf.uxui.util.HttpServletUX;
import com.finger.fwf.uxui.util.UXUtil;
import com.finger.protocol.fwtp.FwtpHeader;
import com.finger.protocol.fwtp.FwtpMessage;

public class UXProvideService extends BasisService {
    private static Logger logger = Logger.getLogger(UXProvideService.class);

    private static UXProvideService _instance = new UXProvideService();

    protected UXProvideService(){
        logger.debug(":: "+ this.getClass() +" ~~~~~~~~~~~~~~~~~~~~~");
    }

    public static UXProvideService getInstance() {
        logger.debug(":: getInstance() ~~~~~~~~~~~~~~~~~~~~~");
        return _instance;
    }

    /**
     * 요청정보를 서버에 전송하고 결과를 리턴받아 오는 서비스 (외부제공용)
     * @param request
     * @param response
     * @throws IOException 
     * @throws ServletException 
     */
    public void doUXService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug(":: doUXService() ~~~~~~~~~~~~~~~~~~~~~");

        HttpServletUX.setResponseContentAndHeader( response );

        FwtpMessage resultMessage = null;

        MsgObj _msg_ = null;
        try {
            //if (getParametersSize(request) > 0) {
            if (getParametersSize(request, logger) > 0)
            {
                String task_id = getReqTaskId(request);	//request.getParameter("_TASK_ID_");

                if ( !UXUtil.isNull(task_id) )
                {
                    FwtpMessage fwtp = new FwtpMessage();
                    fwtp.setClientIp( request.getRemoteAddr() );
                    fwtp.setSysMgtNo( (String)request.getParameter("sys_mgt_no") );
                    fwtp.setUserId( (String)request.getParameter("user_id") );
                    fwtp.setTaskId( task_id );
                    fwtp.setDataType( FwtpHeader._DATATYPE_COMMON );
                    fwtp.setRequestData( parameterExtractList(request) );

                    if ( !fwtp.validateHeaderMap(fwtp.getHeader()) )
                        throw new AgentException("요청을 위한 정보가 유효하지 않습니다.");

                    resultMessage = com.finger.agent.FWFTaskByUXUI.doFWFAgentListForUX ( fwtp );

                    if (FwtpHeader._RESPONSE_OKAY_.equals(resultMessage.getResponseCode()))
                    {
                        toGsonByActionResult(request, response, resultMessage );
                    }
                    else throw new AgentException(resultMessage.getMessageCode(), resultMessage.getErrorMessage());
                }	//if ( !UXUtil.isNull(task_id) )
                else {
                    throw new AgentException(FixedMsg._MSG_TASK_T0000, "시스템담당자에게 문의 바랍니다.");
                }
            }
            else {
                throw new AgentException("요청(request) 파라미터가 하나도 없습니다.");
            }
        } catch (AgentException ae) {
            ae.printStackTrace();
            _msg_ = MsgManager.getInstance().createMessage(ae);
            /*try { response.sendError(500, ee.toString()); } catch (IOException e1) { e1.printStackTrace(); }*/
        } catch (Exception ex) {
            ex.printStackTrace();
            _msg_ = MsgManager.getInstance().createMessage(new AgentException("요청정보 생성 중 오류가 발생하였습니다.", ex));
        } finally {
            toGsonByActionError(request, response, _msg_ );
        }
    }

    protected static final String	__resultCd			=	"result_cd";
    protected static final String	__acntOwnerNm		=	"acnt_owner_nm";

    private void toGsonByActionResult(HttpServletRequest request, HttpServletResponse response, FwtpMessage _message) throws IOException {
        if (response == null || _message == null) return;

        StringBuilder sb = new StringBuilder();
        if (_message.getResponseResultSize() > 0) {
            if ("AL2060010".equals(_message.getTaskId())) {		// AL2060010 | 수취인성명조회
                sb.append(_message.getResponseResultData().get(__resultCd)+",");
                sb.append(_message.getResponseResultData().get(__acntOwnerNm));
            }
            else {
                sb.append("ERROR-잘못된 TASK를 호출하였습니다.");
            }
        }
        else {
            sb.append("ERROR-조회된 결과가 없습니다.");
        }

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( sb.toString().getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }

    public static void toGsonByActionError(HttpServletRequest request, HttpServletResponse response, MsgObj _msg) throws IOException {
        if (response == null || _msg == null) return;

        OutputStream out = null;
        out = response.getOutputStream();
        out.write( ("ERROR-"+ _msg.getDesc()).getBytes(FcAgentConfig._CharSet_) );
        if (out != null) out.close();
    }
}