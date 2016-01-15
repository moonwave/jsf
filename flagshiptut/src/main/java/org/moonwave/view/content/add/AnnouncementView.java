package org.moonwave.view.content.add;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.model.Announcement;
import org.moonwave.util.StackTrace;
import org.moonwave.util.StringUtil;
import org.moonwave.view.BaseView;
import org.moonwave.view.file.FileUploadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Announcement View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class AnnouncementView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(AnnouncementView.class);

    Announcement current;

    @ManagedProperty("#{fileUploadView}")
    private FileUploadView fileUploadView;

    @PostConstruct
    public void init() {
        String selectedId = super.getParameter("selectedId");
        if (selectedId != null) { // edit
            GenericBO<Announcement> bo = new GenericBO<>(Announcement.class);
            current = bo.findById(Integer.valueOf(selectedId));
            fileUploadView.loadUploads4Announcement(current.getUser().getId(), current.getId());
        } else {
            current = new Announcement();
        }
    }

    public Announcement getCurrent() {
        return current;
    }

    public void setCurrent(Announcement current) {
        this.current = current;
    }

    public FileUploadView getFileUploadView() {
        return fileUploadView;
    }

    public void setFileUploadView(FileUploadView fileUploadView) {
        this.fileUploadView = fileUploadView;
    }

    // ========================================================== ActionListener

    public void cancel() throws IOException {
        redirectToListView();
    }

    public void redirectToListView() throws IOException {
        super.redirectTo("/content/add/announcementList.xhtml");
    }

    public String save() {
        if (StringUtil.nullOrEmpty(current.getSubject())) {
            super.error("Subject is empty");
            return null;
        }
        try {
            if (current.getId() == null) {
                current.setUser(super.getLoggedInUser());
                super.getBasebo().persist(current);

                this.fileUploadView.update(super.getLoggedInUser().getId(), null, current.getId(), null);
                this.fileUploadView.save(true);
            } else {
                super.getBasebo().update(current);
                this.fileUploadView.update(super.getLoggedInUser().getId(), null, current.getId(), null);
                this.fileUploadView.save(false);
            }

            // show successful message and reset fields
            super.info("Data save successful");
            this.fileUploadView.clearFields();
            redirectToListView();

        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error(StackTrace.toString(e));
        }
        return null;
    }
}
