package org.moonwave.view.admin;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.moonwave.util.JSFUtil;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StatusView
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class StatusView extends BaseView {

    private static final long serialVersionUID = -1917842415362293541L;

    static final Logger LOG = LoggerFactory.getLogger(StatusView.class);

    String msg = "";
    @PostConstruct
    public void init() {
        if (JSFUtil.getRequestParameterMap().get("msgCode") != null) { // msgCode=101
            JSFUtil.getFlash().put("title", "Action code");
            JSFUtil.getFlash().put("msg", "Your action code was invalid");
            msg = "Your action code was invalid"; // TODO - look up from message.properties 
        }
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
