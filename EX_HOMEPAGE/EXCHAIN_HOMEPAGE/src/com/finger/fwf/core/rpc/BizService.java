package com.finger.fwf.core.rpc;

import com.finger.protocol.fwtp.FwtpMessage;

public interface BizService {
    FwtpMessage callService(FwtpMessage in);
}