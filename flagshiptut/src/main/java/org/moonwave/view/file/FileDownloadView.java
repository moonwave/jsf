package org.moonwave.view.file;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.moonwave.jpa.bo.UploadBO;
import org.moonwave.jpa.model.Upload;
import org.moonwave.util.MimeProperties;
import org.moonwave.util.StackTrace;
import org.moonwave.view.BaseView;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http://www.sitepoint.com/web-foundations/mime-types-complete-list/
 * http://hul.harvard.edu/ois/systems/wax/wax-public-help/mimetypes.htm
 * http://www.freeformatter.com/mime-types-list.html
 * http://stackoverflow.com/questions/12539058/is-there-a-default-mime-type
 * http://stackoverflow.com/questions/1176022/unknown-file-type-mime
 *
 */
@ManagedBean(name = "fileDownloadView")
@ViewScoped
public class FileDownloadView extends BaseView {

    private static final long serialVersionUID = -455047573887351955L;

    static final Logger LOG = LoggerFactory.getLogger(FileDownloadView.class);
    static final String DefaultMimeType = "application/octet-stream";

    private StreamedContent file;
    private List<Upload> downloads;;
    private String filepath = null;

    @PostConstruct
    public void init() {
    }

    public List<Upload> getDownloads() {
        return (downloads != null) ? downloads : new ArrayList<Upload>();
    }

    public void setDownloads(List<Upload> downloads) {
        this.downloads = downloads;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Dynamically download a file which path posted by setFilepath
     *   <p:commandButton value="Download - Dynamic" ajax="false" onclick="PrimeFaces.monitorDownload(start, stop);" icon="ui-icon-arrowthick-1-s">
     *     <f:setPropertyActionListener value="/home/moonwave/Pictures/download-1.pdf" target="#{fileDownloadView.filepath}" />
     *     <p:fileDownload value="#{fileDownloadView.downloadFile}" />
     *   </p:commandButton>
     *
     * @return
     */
    public StreamedContent getDownloadFile() {
        StreamedContent file = null;
        try {
            InputStream stream = new FileInputStream(this.filepath);
            String filename = FilenameUtils.getName(filepath); // filename appeared in download folder

            String extension = FilenameUtils.getExtension(filepath);
            String mimeType = MimeProperties.getInstance().getProperty("." + extension);
            if (mimeType == null) {
                mimeType = DefaultMimeType;
            }

            file = new DefaultStreamedContent(stream, mimeType, filename);
        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        }
        return file;
    }

    public void loadDownloads4Announcement(Integer userId, Integer announcementId) {
        this.downloads = new UploadBO().findByUserAnnouncement(userId, announcementId);
    }

    public void loadDownloads4GroupPost(Integer userId, Integer groupPostId) {
        this.downloads = new UploadBO().findByUserGroupPost(userId, groupPostId);
    }

    // ======================================== The following code are test code

    public StreamedContent getFile() {
        if (file == null) {
            InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/demo/images/optimus.jpg");
            file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.jpg");
        }
        return file;
    }

    public StreamedContent getFile2() {
        StreamedContent file2 = null;
        if (file2 == null) {
            try {
                InputStream stream = new FileInputStream("/home/moonwave/Pictures/icon-1.png");
                file2 = new DefaultStreamedContent(stream, "image/png", "my_downloaded.png");
            } catch (Exception e) {
                LOG.error(StackTrace.toString(e));
            }
        }
        return file2;
    }

    public void download() {
      // TODO
      RequestContext.getCurrentInstance().execute("PrimeFaces.monitorDownload(start, stop);");
    }
}
