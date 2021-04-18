package ir.maktab.finalproject.model.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
 
    OP_INDEX_TICKET,OP_INDEX_CRM,OP_INDEX_ADMIN
    ,OP_CREATE_TICKET,OP_VIEW_TICKET_CRM,OP_ALL_USER_ADMIN
    ,OP_EDIT_TICKET,OP_VIEW_USER_INFO_CRM,OP_SHOW_USER_ADMIN
    ,OP_DELETE_TICKET,OP_ANSWER_TICKET_CRM,OP_EDIT_USER_ADMIN
    ,OP_CLOSE_TICKET,OP_REQUEST_HANDLER_CRM,OP_EDIT_PASSWORD_ADMIN
    ,OP_STEP_TICKET,OP_SUBJECT_MANAGEMENT_CRM,OP_ROLE_MANAGEMENT_ADMIN,OP_CREATE_USER_ADMIN
    ,OP_SEARCH_TICKET,OP_SEARCH_CRM,OP_SEARCH_ADMIN
    ,OP_UPDATE_DOCUMENT_TICKET
    ;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
