package org.moonwave.view.file;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.moonwave.jpa.bo.UploadBO;
import org.moonwave.jpa.model.Upload;
import org.moonwave.util.FileUtil;
import org.moonwave.util.Md5Util;
import org.moonwave.view.BaseView;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "fileUploadView")
@ViewScoped
public class FileUploadView extends BaseView {

    private static final long serialVersionUID = 267212860965791357L;

    static final Logger LOG = LoggerFactory.getLogger(FileUploadView.class);

    private String description;
    private String selectedTag;
    private UploadedFile file;
    private List<Upload> uploads = new ArrayList<>();

    private boolean readyToSave;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(String selectedTag) {
        this.selectedTag = selectedTag;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<Upload> getUploads() {
        return uploads;
    }

    public void setUploads(List<Upload> uploads) {
        this.uploads = uploads;
    }

    public void loadUploads4Announcement(Integer userId, Integer announcementId) {
        this.uploads = new UploadBO().findByUserAnnouncement(userId, announcementId);
    }

    public void loadUploads4GroupPost(Integer userId, Integer groupPostId) {
        this.uploads = new UploadBO().findByUserGroupPost(userId, groupPostId);
    }

    /**
     * Handle basic single file upload, but not commit to database
     * Keep file in hard drive, and in memory, generate 32 digits MD5 tag for
     * possible deletion request
     */
    public void upload() {
        if (file != null) {
            try {
                String filepath = super.getUploadFolder() + "/" + file.getFileName();
                filepath = FileUtil.alternativeFilepathIfExists(filepath);
                FileUtil.createFile(filepath);

                file.write(filepath);
                super.info("Succesful", file.getFileName() + " is uploaded.");

                Upload upload = new Upload();
                upload.setDescription(description);
                upload.setFilepath(filepath);
                upload.setTag(Md5Util.getMd5Sum(filepath));
                this.description = null;
                uploads.add(upload);
                // update ui and ready for save in db
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * Handle advanced multiple files upload
     * @param event
     */
    public void handleFileUpload(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        UploadedFile file = event.getFile();
        try {
            file.write(super.getUploadFolder() + "/" + file.getFileName());
        } catch (Exception e) {
            System.out.println(e);
        }
//        RequestContext.getCurrentInstance().execute("PF('uploadJs').start()");
        System.out.println(event.getFile().getFileName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Handle deletion of single selected uploaded file (identified by 
     * selectedTag) in hard drive and memory
     *
     */
    public void delete() {
        if (this.selectedTag != null) {
            for (int i = uploads.size() -1; i >= 0 ; i--) {
                if (this.selectedTag.equals(uploads.get(i).getTag())) {
                    FileUtil.deleteFile(uploads.get(i).getFilepath());
                    uploads.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * Update upload objects with additional info prior to save
     *
     * @param userId
     * @param tutorGroupId
     * @param announcementId
     * @param groupPostId
     */
    public void update(Integer userId, Short tutorGroupId, Integer announcementId, Integer groupPostId) {
        for (Upload ul: uploads) {
            ul.setUserId(userId);
            ul.setTutorGroupId(tutorGroupId);
            ul.setAnnouncementId(announcementId);
            ul.setGroupPostId(groupPostId);
        }
        readyToSave = true;
    }

    /**
     * Saves uploads to database
     *
     * Note: container view such as AnnouncementView must call update method
     * before calling this save method 
     */
    public void save(boolean create) {
        if (!readyToSave) {
            super.error("Upload objects are not ready to save");
            LOG.error("Upload objects are not ready to save, container view failed to call update method");
        }
        // check duplicate uploads especially in edit mode
        if (create) {
            super.getBasebo().persist(uploads);
        } else {
            super.getBasebo().update(uploads);
        }
        readyToSave = false;
    }

    /**
     * Delete all uploads from hard drive, memory and database
     */
    public void deleteAllFromDB() {
        for (int i = 0; i < uploads.size(); i++) {
            FileUtil.deleteFile(uploads.get(i).getFilepath());
        }
        super.getBasebo().remove(uploads);
    }

    public void clearFields() {
        description = null;
        selectedTag = null;
        file = null;
        uploads.clear();
    }
}
