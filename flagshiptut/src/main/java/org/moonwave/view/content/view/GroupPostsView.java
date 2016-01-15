package org.moonwave.view.content.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.GenericBO;
import org.moonwave.jpa.bo.UploadBO;
import org.moonwave.jpa.model.GroupPost;
import org.moonwave.jpa.model.TutorGroup;
import org.moonwave.jpa.model.Upload;
import org.moonwave.view.AccessController;
import org.moonwave.view.BaseView;
import org.moonwave.view.file.FileDownloadView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GroupPosts View
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class GroupPostsView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(GroupPostsView.class);

    private List<GroupPost> data;
    private GroupPost current;
    private String selectedId;

    @ManagedProperty("#{accessController}")
    private AccessController accessController;

    @ManagedProperty("#{fileDownloadView}")
    private FileDownloadView fileDownloadView;

    // helper fields
    boolean assignment = false;
    String title;

    @PostConstruct
    public void init() {
        String groupPostType = (getParameter("grouppostType") != null) ? getParameter("grouppostType") : "1";
        assignment = GroupPost.ASSIGNMENT.equals(groupPostType);
        if (assignment) {
            title = super.getLocaleLabels().getString("assignment");
        } else {
            title = super.getLocaleLabels().getString("grouppost");
        }
        data = loadAndFilterData();
    }

    public GroupPost getCurrent() {
        return current;
    }

    public void setCurrent(GroupPost current) {
        this.current = current;
    }

    public List<GroupPost> getData() {
        return data;
    }

    public void setGroupposts(List<GroupPost> data) {
        this.data = data;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedPostId) {
        this.selectedId = selectedPostId;
        if ((this.selectedId != null) && !this.selectedId.isEmpty()) {
            GenericBO<GroupPost> bo = new GenericBO<>(GroupPost.class);
            this.current = bo.findById(Integer.parseInt(selectedPostId));
            fileDownloadView.loadDownloads4GroupPost(current.getUser().getId(), current.getId());
        }
    }

    public AccessController getAccessController() {
        return accessController;
    }

    public void setAccessController(AccessController accessController) {
        this.accessController = accessController;
    }

    public FileDownloadView getFileDownloadView() {
        return fileDownloadView;
    }

    public void setFileDownloadView(FileDownloadView fileDownloadView) {
        this.fileDownloadView = fileDownloadView;
    }

    public String hasAttachments(GroupPost a) {
        List<Upload> list = new UploadBO().findByUserGroupPost(a.getUser().getId(), a.getId());
        return list.isEmpty() ? "No" : "Yes";
    }

    public String visibleTo() {
        StringBuilder sb = new StringBuilder();
        if (current != null) {
            List<TutorGroup> tgs = current.getTutorGroups(); 
            if (tgs != null) {
                for (int i = 0; i < tgs.size(); i++) {
                    if (i > 0) {
                        sb.append(", ");
                    }
                    sb.append(tgs.get(i).getAlias());
                }
            }
        }
        return sb.toString();
    }

    // ========================================================== Helper methods

    public boolean isAssignment() {
        return assignment;
    }

    public String getTitle() {
        return title;
    }

    // ========================================================= Private methods

    /**
     * only show published GroupPosts in default date range, ordered by latest
     * to oldest; filtered by posted tutor group
     */
    private List <GroupPost> loadAndFilterData() {
        List<TutorGroup> userGroups = super.getLoggedInUser().getTutorGroups();
        Set<Short> userGroupsSet = new HashSet<>();
        for (TutorGroup tg : userGroups) {
            userGroupsSet.add(tg.getId());
        }
        GenericBO<GroupPost> bo = new GenericBO<>(GroupPost.class);
        List <GroupPost> list = bo.findAllInDateRange(); 
        List <GroupPost> ret = new ArrayList<GroupPost>();
        for (GroupPost gp : list) {
            if (assignment && !gp.isAssignment()) {
                continue;
            }
            if (!assignment && !gp.isRegularPost()) {
                continue;
            }
            if (gp.getPublished()) {
                List<TutorGroup> tgs = gp.getTutorGroups();
                if ((tgs == null) || tgs.isEmpty()) {
                    continue;
                }
                for (TutorGroup tg : tgs) {
                    if (userGroupsSet.contains(tg.getId())) {
                        ret.add(gp);
                        break;
                    }
                }
            }
        }
        return ret;
    }
}