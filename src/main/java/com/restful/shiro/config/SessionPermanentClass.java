package com.restful.shiro.config;

import com.restful.shiro.mapper.SessionCRUDMapper;
import com.restful.shiro.util.SerializableUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description SessionPermanentClass
 * @date 2019-09-19 14:15
 */
@Deprecated
public class SessionPermanentClass extends EnterpriseCacheSessionDAO {

    @Autowired
    private SessionCRUDMapper sessionCRUDMapper;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        sessionCRUDMapper.doCreate((String) sessionId, SerializableUtils.serialize(session));
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        String session = sessionCRUDMapper.doReadSession((String) sessionId);
        if (session == null) {
            return null;
        }
        return SerializableUtils.deserialize(session);
    }

    @Override
    protected void doUpdate(Session session) {
        if (session instanceof ValidatingSession
                && !((ValidatingSession) session).isValid()) {
            return;
        }
        Serializable sessionId = generateSessionId(session);
        if (sessionId != null) {
            sessionCRUDMapper.doUpdate(SerializableUtils.serialize(session), (String) sessionId);
        }
    }

    @Override
    protected void doDelete(Session session) {
        Serializable sessionId = generateSessionId(session);
        if (sessionId != null) {
            sessionCRUDMapper.doDelete((String) sessionId);
        }
    }
}
