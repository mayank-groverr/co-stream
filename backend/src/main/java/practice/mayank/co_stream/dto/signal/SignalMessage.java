package practice.mayank.co_stream.dto.signal;

import practice.mayank.co_stream.entity.CallType;
import practice.mayank.co_stream.entity.SignalType;

public record SignalMessage(
        SignalType type,
        String callId,
        String roomId,
        String from,
        String to,
        CallType callType,
        Object payload
) {}