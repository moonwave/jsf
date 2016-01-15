package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UploadBO
 *
 * @author moonwave
 *
 */
public class UploadBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(UploadBO.class);

    public List<Upload> findAllUploads() {
        Query query = super.getEntityManager().createNamedQuery("Upload.findAll", Upload.class);
        @SuppressWarnings("unchecked")
        List<Upload> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Upload> findByUserAnnouncement(Integer userId, Integer announcementId) {
        Query query = super.getEntityManager().createNamedQuery("Upload.findByUserAnnouncement", Upload.class);
        query.setParameter("userId", userId);
        query.setParameter("announcementId", announcementId);
        List<Upload> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
	public List<Upload> findByUserGroupPost(Integer userId, Integer groupPostId) {
        Query query = super.getEntityManager().createNamedQuery("Upload.findByUserGroupPost", Upload.class);
        query.setParameter("userId", userId);
        query.setParameter("groupPostId", groupPostId);
        List<Upload> list = query.getResultList();
        super.release();
        return list;
    }

    public Upload findById(Integer id) {
        Query query = super.getEntityManager().createNamedQuery("Upload.findById", Upload.class);
        query.setParameter("id", id);
        Upload ret = null;
        try {
            ret = (Upload) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }
}
