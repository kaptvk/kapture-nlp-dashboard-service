package com.kapturecrm.nlpqueryengineservice.repository;


import com.kapturecrm.customer.objects.Customer;
import com.kapturecrm.nlpdashboardservice.utility.ClickHouseConnUtil;
import com.kapturecrm.object.Employee;
import com.kapturecrm.ticket.objects.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class MysqlToClickhouseRepo {

    @Autowired
    SessionFactory sessionFactory;


    public void saveTicket(Ticket ticket) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ClickHouseConnUtil.getConnection();
            if (connection == null) {
                log.error("Connection is null");
                return;
            }
            String insertQuery = "INSERT INTO cm_general_task (cm_id, task_title, assigned_to_id, assigned_to_name, creator_id, creator_name, detail, date, task_end_date, last_follow_up, next_follow_up, status, enabled, is_periodic, periodicity, type, reference_id, pending_task_id, category_id, member_id, task_repeat_weekdays, send_email, send_message, task_type, client_emp_id, agency_brief_id, agency_creative_req_id, agency_content_req_id, product_id, last_modification_time, latitude, longitude, repeat_interval, associative_file, ticket_id, contact_id, location, address, problem_solution, complaint, chargeable, service_charge, update_task, priority, last_conversation_time, enquiry_id, ticket_email, folder_id, sub_status, customer_feedback, facebook_user_id, twitter_user_id, play_store_review_id, is_out_of_sla, queue_key, last_conversation_type, last_conversation_id, total_conversation_count, order_id, city, hub, lob, comment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            statement = connection.prepareStatement(insertQuery);
            statement.setInt(1, ticket.getCmId());
            statement.setString(2, ticket.getTaskTitle());
            statement.setInt(3, ticket.getAssignedToId());
            statement.setString(4, ticket.getAssignedToName());
            statement.setInt(5, ticket.getCreatorId());
            statement.setString(6, ticket.getCreatorName());
            statement.setString(7, ticket.getDetail());
            statement.setTimestamp(8, ticket.getDate());
            statement.setTimestamp(9, ticket.getTaskEndDate());
            statement.setTimestamp(10, ticket.getLastFollowUp());
            statement.setTimestamp(11, ticket.getNextFollowUp());
            statement.setString(12, String.valueOf(ticket.getStatus()));
            statement.setBoolean(13, ticket.getEnabled());
            statement.setBoolean(14, ticket.getIsPeriodic());
            statement.setString(15, ticket.getPeriodicity());
            statement.setString(16, String.valueOf(ticket.getType()));
            statement.setLong(17, ticket.getReferenceId());
            statement.setInt(18, ticket.getPendingTaskId());
            statement.setInt(19, ticket.getCategoryId());
            statement.setInt(20, ticket.getMemberId());
            statement.setString(21, ticket.getTaskRepeatWeekdays());
            statement.setBoolean(22, ticket.getSendEmail());
            statement.setBoolean(23, ticket.getSendMessage());
            statement.setString(24, ticket.getTaskType());
            statement.setInt(25, ticket.getClientEmpId());
            statement.setInt(26, ticket.getAgencyBriefId());
            statement.setInt(27, ticket.getAgencyCreativeReqId());
            statement.setInt(28, ticket.getAgencyContentReqId());
            statement.setInt(29, ticket.getProductId());
            statement.setTimestamp(30, ticket.getLastModificationTime());
            statement.setString(31, ticket.getLatitude());
            statement.setString(32, ticket.getLongitude());
            statement.setInt(33, ticket.getRepeatInterval());
            statement.setString(34, ticket.getAssociativeFile());
            statement.setString(35, ticket.getTicketId());
            statement.setInt(36, ticket.getContactId());
            statement.setString(37, ticket.getLocation());
            statement.setString(38, ticket.getAddress());
            statement.setString(39, ticket.getProblemSolution());
            statement.setString(40, ticket.getComplaint());
            statement.setBoolean(41, ticket.getChargeable());
            statement.setDouble(42, ticket.getServiceCharge());
            statement.setBoolean(43, ticket.getUpdateTask());
            statement.setInt(44, ticket.getPriority());
            statement.setLong(45, ticket.getLastConversationTime());
            statement.setInt(46, ticket.getEnquiryId());
            statement.setString(47, ticket.getTicketEmail());
            statement.setInt(48, ticket.getFolderId());
            statement.setString(49, ticket.getSubStatus());
            statement.setInt(50, ticket.getCustomerFeedback());
            statement.setString(51, ticket.getFacebookUserId());
            statement.setString(52, ticket.getTwitterUserId());
            statement.setString(53, ticket.getPlayStoreReviewId());
            statement.setBoolean(54, ticket.getIsOutOfSla());
            statement.setString(55, ticket.getQueueKey());
            statement.setString(56, String.valueOf(ticket.getLastConversationType()));
            statement.setInt(57, ticket.getLastConversationId());
            statement.setInt(58, ticket.getTotalConversationCount());
            statement.setString(59, ticket.getOrderId());
            statement.setString(60, ticket.getCity());
            statement.setString(61, ticket.getHub());
            statement.setString(62, ticket.getLob());
            statement.setString(63, ticket.getComment());

            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error in saveTicket", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Error in closing resources", e);
            }
        }
    }

    public List<Ticket> getTicket(int cmId) {
        Session session = null;
        List<Ticket> ticket = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            ticket = session.createQuery("from Ticket where cmId = :cmId", Ticket.class).setParameter("cmId", cmId).setFirstResult(2).setMaxResults(1000).getResultList();
        } catch (Exception ex) {
            log.error("Error in getPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return ticket;
    }

    public List<Customer> getCustomer(int cmId) {
        Session session = null;
        List<Customer> customers = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            customers = session.createQuery("from Customer where cmId = :cmId", Customer.class).setParameter("cmId", cmId).getResultList();
        } catch (Exception ex) {
            log.error("Error in getPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return customers;
    }

    public void saveCustomer(Customer customer) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ClickHouseConnUtil.getConnection();
            if (connection == null) {
                log.error("Connection is null");
                return;
            }
            String query = "INSERT INTO cm_lead_member (cm_id, is_organization, utf_name1, company_phone, website, address, state, city, country, latitude, longitude, current_status, generation_date, creator_id, creator_name, channel_partner_id, remarks, rate_plan_id, customer_code, city_potential, hotel_potential, segment, key_markets, hotels_used, price_point, key_foreign_tour_operators, key_business, is_channel_partner, pin_code, source_remarks, locality, campaign_id, campaign_item_id, campaign_activity_id, contact_type, offer, mtm_sv_feedback, last_modification_time, image_reference_id, classification, customer_type, customer_level, zone, company_parent_id, assigned_to_id, assigned_to_name, land_line, customer_sub_type, attr1, attr2, attr3, attr4, attr5, attr6, attr7, attr8, attr9, attr10, attr11, attr12, attr13, attr14, attr15, attr16, attr17, attr18, attr19, attr20, rate_quotation_url, max_credit_amount, max_credit_period, customer_referal_code, company_status, sync_status, syncd_location_id, cross_remarks, synced_step_number, out_standing_amount, sales_admin_zone, rate_contract_html, referral_id) " + "VALUES " + " " + " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customer.getCmId());
            preparedStatement.setBoolean(2, customer.getIsOrganization());
            preparedStatement.setString(3, customer.getName());
            preparedStatement.setString(4, customer.getCompanyPhone());
            preparedStatement.setString(5, customer.getWebsite());
            preparedStatement.setString(6, customer.getAddress());
            preparedStatement.setString(7, customer.getState());
            preparedStatement.setString(8, customer.getCity());
            preparedStatement.setString(9, customer.getCountry());
            preparedStatement.setString(10, customer.getLatitude());
            preparedStatement.setString(11, customer.getLongitude());
            preparedStatement.setString(12, customer.getCurrentStatus().toString()); // Assuming currentStatus is of type Character
            preparedStatement.setTimestamp(13, customer.getGenerationDate());
            preparedStatement.setInt(14, customer.getCreatorId());
            preparedStatement.setString(15, customer.getCreatorName());
            preparedStatement.setInt(16, customer.getChannelPartnerId());
            preparedStatement.setString(17, customer.getRemarks());
            preparedStatement.setInt(18, customer.getRatePlanId());
            preparedStatement.setString(19, customer.getCustomerCode());
            preparedStatement.setString(20, customer.getCityPotential());
            preparedStatement.setString(21, customer.getHotelPotential());
            preparedStatement.setString(22, customer.getSegment());
            preparedStatement.setString(23, customer.getKeyMarkets());
            preparedStatement.setString(24, customer.getHotelsUsed());
            preparedStatement.setString(25, customer.getPricePoint());
            preparedStatement.setString(26, customer.getKeyForeignTourOperators());
            preparedStatement.setString(27, customer.getKeyBusiness());
            preparedStatement.setBoolean(28, customer.getIsChannelPartner());
            preparedStatement.setString(29, customer.getPinCode());
            preparedStatement.setString(30, customer.getSource());
            preparedStatement.setString(31, customer.getLocality());
            preparedStatement.setInt(32, customer.getCampaignId());
            preparedStatement.setInt(33, customer.getCampaignItemId());
            preparedStatement.setInt(34, customer.getCampaignActivityId());
            preparedStatement.setString(35, customer.getFirstContactType());
            preparedStatement.setString(36, customer.getOffer());
            preparedStatement.setString(37, customer.getFeedbackOnCustomer());
            preparedStatement.setTimestamp(38, new Timestamp(customer.getLastModificationTime().getTimeInMillis()));
            preparedStatement.setString(39, customer.getImageReferenceId());
            preparedStatement.setString(40, customer.getClassification());
            preparedStatement.setString(41, customer.getCustomerType());
            preparedStatement.setInt(42, customer.getCustomerLevel());
            preparedStatement.setInt(43, customer.getZone());
            preparedStatement.setInt(44, customer.getCompanyParentId());
            preparedStatement.setInt(45, customer.getAssignedToId());
            preparedStatement.setString(46, customer.getAssignedToName());
            preparedStatement.setString(47, customer.getLandLine());
            preparedStatement.setString(48, customer.getCustomerSubType());
            preparedStatement.setString(49, customer.getAttr1());
            preparedStatement.setString(50, customer.getAttr2());
            preparedStatement.setString(51, customer.getAttr3());
            preparedStatement.setString(52, customer.getAttr4());
            preparedStatement.setString(53, customer.getAttr5());
            preparedStatement.setString(54, customer.getAttr6());
            preparedStatement.setString(55, customer.getAttr7());
            preparedStatement.setString(56, customer.getAttr8());
            preparedStatement.setString(57, customer.getAttr9());
            preparedStatement.setString(58, customer.getAttr10());
            preparedStatement.setString(59, customer.getAttr11());
            preparedStatement.setString(60, customer.getAttr12());
            preparedStatement.setString(61, customer.getAttr13());
            preparedStatement.setString(62, customer.getAttr14());
            preparedStatement.setString(63, customer.getAttr15());
            preparedStatement.setString(64, customer.getAttr16());
            preparedStatement.setString(65, customer.getAttr17());
            preparedStatement.setString(66, customer.getAttr18());
            preparedStatement.setString(67, customer.getAttr19());
            preparedStatement.setString(68, customer.getAttr20());
            preparedStatement.setString(69, customer.getRateQuotationUrl());
            preparedStatement.setDouble(70, customer.getMaxCreditAmt());
            preparedStatement.setInt(71, customer.getMaxCreditPeriod());
            preparedStatement.setString(72, customer.getReferalCode());
            preparedStatement.setString(73, customer.getCompanyStatus());
            preparedStatement.setBoolean(74, customer.getSyncStatus());
            preparedStatement.setString(75, customer.getSyncdLocationId());
            preparedStatement.setString(76, customer.getCrossRemarks());
            preparedStatement.setInt(77, customer.getSyncedStepNumber());
            preparedStatement.setDouble(78, customer.getOutStandingAmount());
            preparedStatement.setInt(79, customer.getSalesAdminZone());
            preparedStatement.setString(80, customer.getRateContractHtml());
            preparedStatement.setInt(81, customer.getReferralId());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("Error in saveCustomer", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Error in closing resources", e);
            }
        }
    }

    public void saveEmployee(Employee employee) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ClickHouseConnUtil.getConnection();

            String sql = "INSERT INTO cm_employee (" + "id, cm_id, name, email, emp_code, addtional_mail_ids, phone, address, permanent_address, designation, " + "created_by, updated_by, join_date, appointment, privileges, activate, photo_path, designation_ids, " + "inbox_id, area_code_id, email_credential_id, emp_roll_id, parent_emp_id, media_plan_campaign_cm_id, " + "lead_contest_probability, lead_count, shift_id, media_agency_id, is_media_agency, desk_id, city, " + "company_name, speciality, special_skills, awards_and_recognition, company_logo, summary, experience_text, " + "facebook_link, twitter_link, linkedin_link, openfire_id, openfire_pass, zone_id, mobile_photo_path, " + "disable_by, disable_time, last_modification_time, lead_allocation_probability, work_order_approval_limit, " + "checked_in_customer_id, sales_force_id, sales_group_id, territory_id, parent_zone_id, emp_email_template, " + "email_signature, copy_emp_hierarchy, emp_email_attachment, testimonial_link, blog_link, profile_status, " + "country, is_admin, discount_limit, device_id, mobilecrm_device_id, mobilecrm_reg_id, commission_plan_id, " + "product_ids, folder_ids, write_kapdrive_folder_ids, read_kapdrive_folder_ids, mobile_crm_version, " + "google_access_token, google_refresh_token, mobilecrm_device_name, queue_key, mobilecrm_push_data, " + "mobilecrm_pull_data, mobilecrm_last_sync_time, customer_type_ids, time_zone, google_synced_email_id, " + "google_synced_email_name, ip_addess_list, leave_balance, state, default_currency, upload_status, " + "upload_status_message, user_id, password, send_email_on_adding, gender, two_factor_auth, attr1, attr2, " + "attr3, attr4, attr5, attr6, attr7, attr8, attr9, attr10, attr11, attr12, attr13, attr14, attr15, attr16, " + "attr17, attr18, attr19, attr20, attr21, attr22, attr23, attr24, attr25, attr26, attr27, attr28, attr29, attr30" + ") VALUES " + "(?,?,?,,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ,?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            assert connection != null;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, employee.getId());
            statement.setInt(2, employee.getCmId());
            statement.setString(3, employee.getName());
            statement.setString(4, employee.getEmail());
            statement.setString(5, employee.getEmpCode());
            statement.setString(6, employee.getAddtionalMailIds());
            statement.setString(7, employee.getPhone());
            statement.setString(8, employee.getAddress());
            statement.setString(9, employee.getPermanentAddress());
            statement.setString(10, employee.getDesignation());
            statement.setInt(11, employee.getCreatedBy());
            statement.setInt(12, employee.getUpdatedBy());
            statement.setTimestamp(13, employee.getJoinDate());
            statement.setBoolean(14, employee.isAppointment());
            statement.setString(15, employee.getPrivileges());
            statement.setBoolean(16, employee.isActivate());
            statement.setString(17, employee.getPhotoPath());
            statement.setString(18, employee.getDesignationIds());
            statement.setInt(19, employee.getInboxId());
            statement.setString(20, employee.getAreaCodeId());
            statement.setString(21, employee.getEmailCredentialId());
            statement.setInt(22, employee.getEmployeeRollId());
            statement.setInt(23, employee.getParentEmpId());
            statement.setString(24, employee.getMediaPlanCampaignCmId());
            statement.setDouble(25, employee.getLeadContestProbability());
            statement.setInt(26, employee.getLeadCount());
            statement.setInt(27, employee.getShiftId());
            statement.setInt(28, employee.getMediaAgencyId());
            statement.setBoolean(29, employee.isMediaAgency());
            statement.setString(30, employee.getDeskId());
            statement.setString(31, employee.getCity());
            statement.setString(32, employee.getCompanyName());
            statement.setString(33, employee.getSpeciality());
            statement.setString(34, employee.getSpecialSkills());
            statement.setString(35, employee.getAwardsAndRecognition());
            statement.setString(36, employee.getCompanyLogo());
            statement.setString(37, employee.getSummary());
            statement.setString(38, employee.getExperienceText());
            statement.setString(39, employee.getFacebookLink());
            statement.setString(40, employee.getTwitterLink());
            statement.setString(41, employee.getLinkedInLink());
            statement.setString(42, employee.getOpenfireId());
            statement.setString(43, employee.getOpenfirePassword());
            statement.setInt(44, employee.getZoneId() != null ? employee.getZoneId() : 0);
            statement.setString(45, employee.getMobilePhotoPath());
            statement.setInt(46, employee.getDisableBy());
            statement.setTimestamp(47, employee.getDisableTime());
            statement.setTimestamp(48, new Timestamp(employee.getLastModificationTime().getTimeInMillis()));
            statement.setInt(49, employee.getLeadAllocationProbability());
            statement.setDouble(50, employee.getWorkOrderApprovalLimit());
            statement.setInt(51, employee.getCheckedInCustomerId());
            statement.setString(52, employee.getSalesForceId());
            statement.setString(53, employee.getSalesGroupId());
            statement.setString(54, employee.getTerritoryId());
            statement.setInt(55, employee.getParentZoneId() != null ? employee.getParentZoneId() : 0);
            statement.setString(56, employee.getEmpEmailTemplate());
            statement.setString(57, employee.getEmailSignature());
            statement.setInt(58, employee.getCopyEmpHierarchy());
            statement.setString(59, employee.getEmpEmailAttachment());
            statement.setString(60, employee.getTestimonialLink());
            statement.setString(61, employee.getBlogLink());
            statement.setString(62, employee.getProfileStatus());
            statement.setString(63, employee.getCountry());
            statement.setBoolean(64, employee.isAdmin());
            statement.setDouble(65, employee.getDiscountLimit());
            statement.setString(66, employee.getDeviceId());
            statement.setString(67, employee.getMobileCRMDeviceId());
            statement.setString(68, employee.getMobileCRMGcmRegId());
            statement.setInt(69, employee.getCommissionId());
            statement.setString(70, employee.getProductIdStr());
            statement.setString(71, employee.getFolderIds());
            statement.setString(72, employee.getWriteKapDriveFolderIds());
            statement.setString(73, employee.getReadKapDriveFolderIds());
            statement.setString(74, employee.getMobileCRMVersion());
            statement.setString(75, employee.getGoogleAccessToken());
            statement.setString(76, employee.getGoogleRefreshToken());
            statement.setString(77, employee.getMobileCrmDeviceName());
            statement.setString(78, employee.getQueueKey());
            statement.setInt(79, employee.getMobileCrmPushData() != null ? employee.getMobileCrmPushData() : 0);
            statement.setInt(80, employee.getMobileCrmPullData() != null ? employee.getMobileCrmPullData() : 0);
            statement.setTimestamp(81, new Timestamp(employee.getMobileCrmLastSyncTime() != null ? employee.getMobileCrmLastSyncTime().getTimeInMillis() : 0L));
            statement.setString(82, employee.getCustomerTypeIdStr() != null ? employee.getCustomerTypeIdStr() : "");
            statement.setString(83, employee.getTimeZone());
            statement.setString(84, employee.getGoogleSyncedEmailId());
            statement.setString(85, employee.getGoogleSyncedEmailName());
            statement.setString(86, employee.getIpAddressList());
            statement.setDouble(87, employee.getLeaveBalance());
            statement.setString(88, employee.getState());
            statement.setString(89, employee.getCurrency());
            statement.setString(90, employee.getUploadStatus());
            statement.setString(91, employee.getUploadStatusMessage());
            statement.setString(92, employee.getUserId());
            statement.setString(93, employee.getPassword());
            statement.setBoolean(94, employee.isSendEmailOnAdding() != null ? employee.isSendEmailOnAdding() : false);
            statement.setString(95, employee.getGender() + "");
            statement.setBoolean(96, employee.isTwoFactorAuth());
            statement.setString(97, employee.getAttr1());
            statement.setString(98, employee.getAttr2());
            statement.setString(99, employee.getAttr3());
            statement.setString(100, employee.getAttr4());
            statement.setString(101, employee.getAttr5());
            statement.setString(102, employee.getAttr6());
            statement.setString(103, employee.getAttr7());
            statement.setString(104, employee.getAttr8());
            statement.setString(105, employee.getAttr9());
            statement.setString(106, employee.getAttr10());
            statement.setString(107, employee.getAttr11());
            statement.setString(108, employee.getAttr12());
            statement.setString(109, employee.getAttr13());
            statement.setString(110, employee.getAttr14());
            statement.setString(111, employee.getAttr15());
            statement.setString(112, employee.getAttr16());
            statement.setString(113, employee.getAttr17());
            statement.setString(114, employee.getAttr18());
            statement.setString(115, employee.getAttr19());
            statement.setString(116, employee.getAttr20());
            statement.setString(117, employee.getAttr21());
            statement.setString(118, employee.getAttr22());
            statement.setString(119, employee.getAttr23());
            statement.setString(120, employee.getAttr24());
            statement.setString(121, employee.getAttr25());
            statement.setString(122, employee.getAttr26());
            statement.setString(123, employee.getAttr27());
            statement.setString(124, employee.getAttr28());
            statement.setString(125, employee.getAttr29());
            statement.setString(126, employee.getAttr30());
            statement.executeUpdate();
        } catch (Exception e) {
            log.error("Error in saveEmployee", e);
            System.out.println(e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                log.error("Error in closing resources", e);
            }
        }
    }

    public List<Employee> getEmployee(int cmId) {
        Session session = null;
        List<Employee> employee = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            employee = session.createQuery("from Employee where cmId = :cmId", Employee.class).setParameter("cmId", cmId).getResultList();
        } catch (Exception ex) {
            log.error("Error in getPrompt", ex);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return employee;
    }
}



