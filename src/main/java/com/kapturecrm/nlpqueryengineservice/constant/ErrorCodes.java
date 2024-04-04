package com.kapturecrm.nlpqueryengineservice.constant;

public enum ErrorCodes {
    BAD_EMPLOYEE_AREA_CODE("No area code mapped to Employee"),
    CAUGHT_EXCEPTION("Exception caught, please contact support."),
    EMPTY_INTERNAL_ORDER_ENTITY_LIST("InternalOrderEntityList can not be empty."),
    EMPTY_REQUEST_BODY("The request body is empty."),
    EMPTY_STRING("String can not be left blank"),
    ERROR_READING_FILE("IOException caught while reading file"),
    ERROR_WHILE_VALIDATING_ENTITY_LIST("Error while validating Entity list."),
    GODOWNID_CANNOT_BE_ZERO("godownId should be greater than 0."),
    GODOWN_ALREADY_EXISTS("Name or zone already exists."),
    GODOWN_DOES_NOT_EXIST("No godown exists."),
    INVALID_BODY("Invalid request, please ensure all parameters are correct"),
    INVALID_DATE("The date field(s) is/are in invalid format. It should be in yyyy-MM-dd format."),
    INVALID_DATE_RANGE("Either StartDate or EndDate format is invalid or startDate exceeds endDate"),
    INVALID_DATE_TIME("The date time field(s) is/are in invalid format. It should be in yyyy-MM-dd HH:mm:ss format."),
    INVALID_ENTITY_LIST_AFTER_DB_VALIDATION("Invalid Entity list after database validation (Priority orders)"),
    INVALID_ENTITY_LIST_AFTER_VALIDATION("Invalid Entity list after validation"),
    INVALID_EXCEL_FILE("Excel file is either invalid or contains zero rows"),
    INVALID_ID("Id should be greater than or equal to zero."),
    INVALID_INTERNAL_ORDER_ID("Internal Order Id should be greater than zero"),
    INVALID_MULTIPART("Not a valid multipart request."),
    INVALID_NAME("Name can not be left blank."),
    INVALID_PRODUCT_ID_FOR_ENTITY_LIST("Invalid productId for Entity List"),
    INVALID_ZONE_ID("ZoneId should be greater than zero."),
    ZONE_ID_NOT_EXIST("No zone mapped to zoneId."),
    JSON_PARSE_ERROR("Error parsing JSON body. Make sure that all fields are valid."),
    JSON_PROCESSING_ERROR("Encountered an error while in object Mapper"),
    NOT_A_VALID_EXCEL("Not a valid excel file."),
    NO_EMPLOYEE_FOUND("Invalid employee, can not proceed further"),
    NO_ENTITY_LIST_EXISTS("Error approving invoice details with no entities."),
    NO_INTERNAL_ORDER_EXISTS("No internalOrder exists"),
    NO_INVOICE_DETAILS_FOUND("No Invoice details found"),
    NO_ORDER_EXISTS("No Order mapped to Employee"),
    NO_PENDING_INVOICE_FOUND("No pending invoice found."),
    NO_PRODUCT_BATCH_EXISTS("No batch exists"),
    NO_PRODUCT_DETAILS_EXIST("No product details exist"),
    NO_PRODUCT_STOCK_EXISTS("No Product Stock exists"),
    NO_RESULT_EXISTS("No result found for given parameter(s)"),
    NO_VALID_REQUEST("No valid request exists."),
    NO_VALID_REQUEST_AFTER_DB_VALIDATION("No valid request exists after database validation"),
    NO_VALID_REQUEST_AFTER_VALIDATION("No valid request exists after validation."),
    NULL_ORDER_FUNCTION("Order Function cannot be null"),
    QUANTITY_GREATER_THAN_IN_STOCK("Quantity can not be greater than present in current stock"),
    RECORD_DOES_NOT_EXIST_IN_DATABASE("No matching records found"),
    STOCK_ALREADY_DEDUCTED("Stock has already been deducted"),
    UNAUTHORIZED_ACCESS("Unauthorized Access (FORBIDDEN)"),
    NO_RECORDS_FOUND("No records found"),

    /**
     * CUSTOMER ERROR CODES
     **/

    DUPLICATE_ENGINEER_MAPPING("Service Engineer is already mapped!"),
    DUPLICATE_PINCODE_MAPPING("Pincode is already mapped!"),
    INVALID_PINCODE_TYPE("Pincode type cannot be null"),
    INVALID_SERVICE_ENGINEER("Invalid Service Engineer"),
    INVALID_PRODUCT_IDS_FOUND("Invalid ProductIds found"),
    PRODUCT_BATCH_DOES_NOT_EXIST("No Product Batch exists."),
    INVALID_EMAIL("Invalid Email Address");
    private String message = "";

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage(String n) {
        return message + " " + n;
    }

}
