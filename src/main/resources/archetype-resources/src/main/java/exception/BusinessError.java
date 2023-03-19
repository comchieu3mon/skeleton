#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of business error codes.
 *
 * @author duccaom
 * @version 1.0
 * @since 2022/3/30
 */

/* Convention:

 * Error code pattern: SRX-YZZZ
 * with:
 *     X: Repo name
 *     Y: Entity number (refer at Response messages confluence page)
 *     ZZZ:Incremental number
 * example: SRS-0001
 *
 * Message key pattern: error.<entity_name>.<error_type>
 * example: error.plan_frequency.not_found
 */

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BusinessError {

  ENTITY_NOT_FOUND("SRS-0001", "error.entity.not_found"),

  ENTITY_EXISTED("SRS-0002", "error.entity.existed"),

  SUBSCRIPTION_NOT_ACTIVE("SRS-1001", "error.subscription.not_active"),
  SUBSCRIPTION_NEXT_TRIGGER_DATE_NOT_IN_FUTURE("SRS-1002",
      "error.subscription.next_trigger_date_not_in_future"),
  SUBSCRIPTION_ORGANIZATION_NOT_FOUND("SRS-1003", "error.subscription.organization_not_found"),
  SUBSCRIPTION_SITE_NOT_FOUND("SRS-1004", "error.subscription.site_not_found"),
  SUBSCRIPTION_LOCALE_NOT_FOUND("SRS-1005", "error.subscription.locale_not_found"),
  SUBSCRIPTION_PLAN_NOT_FOUND("SRS-1006", "error.subscription.plan_not_found"),
  SUBSCRIPTION_EXTERNAL_ORDER_ID_EXISTED("SRS-1007",
      "error.subscription.external_order_id_existed"),
  SUBSCRIPTION_PRODUCT_NOT_BELONG_TO_ORGANIZATION("SRS-1008",
      "error.subscription.product_not_belong_to_organization"),
  SUBSCRIPTION_ALREADY_CANCELLED_OR_COMPLETED("SRS-1009",
      "error.subscription.already_canceled_or_completed"),
  TRIGGER_DATE_REQUEST_IS_NOT_ACCEPTED("SRS-1010", "error.trigger_date.is_not_accepted"),
  TRIGGER_DATE_IS_NOT_VALID("SRS-1011", "error.trigger_date.is_invalid"),
  PLAN_FREQUENCY_TYPE_MUST_MONTH("SRS-1012", "error.subscription.plan_frequency_must_be_month"),
  PLAN_FREQUENCY_TYPE_NOT_SUPPORTED("SRS-1013", "error.subscription.plan_frequency_not_supported"),
  SUBSCRIPTION_CAN_NOT_SENT_ORDER_TO_OM("SRS-1014", "error.subscription.can_not_send_order_to_OM"),

  ORG_IS_CHANGED("SRS-2001", "error.organization.is_changed"),


  SITE_ORG_IS_CHANGED("SRS-3001", "error.site.organization_is_changed"),


  LOCALE_ORG_IS_CHANGED("SRS-4001", "error.locale.organization_is_changed"),
  LOCALE_SITE_IS_CHANGED("SRS-4002", "error.locale.site_is_changed"),

  PLAN_FREQUENCY_TYPE_INCOMPATIBLE("SRS-5001", "error.plan_frequency.type_incompatible"),
  PLAN_FREQUENCY_HAS_ACTIVE_SUBSCRIPTION("SRS-5002",
      "error.plan_frequency.has_active_subscription"),
  CANCELLATION_POLICY_NOT_FOUND_OR_INACTIVE("SRS-5003",
      "error.plan_frequency.cancellation_policy_not_found_or_inactive"),
  DELAY_POLICY_NOT_FOUND_OR_INACTIVE("SRS-5004",
      "error.plan_frequency.delay_policy_not_found_or_inactive"),
  SKIP_POLICY_NOT_FOUND_OR_INACTIVE("SRS-5005",
      "error.plan_frequency.skip_policy_not_found_or_inactive"),

  LOCALE_NOT_FOUND_OR_INACTIVE("SRS-6001", "error.plan.locale_not_found_or_inactive"),
  PLAN_HAS_ACTIVE_SUBSCRIPTION("SRS-6002", "error.plan.has_active_subscription"),

  START_DATE_GREATER_THAN_END_DATE("SRS-7001", "error.plan_access.startdate_greater_than_enddate"),
  PLAN_ACCESS_DATE_END_BEFORE_START("SRS-7002", "error.plan_access.date_end_before_start"),
  PLAN_ACCESS_DATE_START_BEFORE_NOW("SRS-7003", "error.plan_access.date_start_before_now"),
  PLAN_ACCESS_DATE_END_BEFORE_NOW("SRS-7004", "error.plan_access.date_end_before_now"),
  PLAN_ACCESS_DATE_IS_PASSED("SRS-7005", "error.plan_access.date_passed"),

  SUB_CAN_START_DATE_MUST_AFTER("SRS-8001",
      "error.subscription_cancellation.start_date_must_after"),
  SUB_CAN_CREATED_DATE_MUST_TODAY("SRS-8002",
      "error.subscription_cancellation.created_date_must_today"),
  SUB_CAN_SUBSCRIPTION_NOT_VALID("SRS-8003",
      "error.subscription_cancellation.subscription_not_valid"),
  SUB_CAN_SOURCE_NOT_SUPPORTED("SRS-8004", "error.subscription_cancellation.source_not_supported"),
  SUB_CAN_UNABLE_FORCE_CANCEL_ON_SOURCE("SRS-8005",
      "error.subscription_cancellation.unable_force_can_on_source"),
  SUB_CAN_UNABLE_CANCEL_ON_MY_ACCOUNT("SRS-8006",
      "error.subscription_cancellation.unable_cancel_on_my_account"),

  SKIP_SUB_NOT_SKIPPED("SRS-9001", "error.skip.sub_could_not_skipped"),
  SKIP_FORCE_SOURCE_INVALID("SRS-9002", "error.skip.invalid_force_source"),
  SKIP_SOURCE_INVALID("SRS-9003", "error.skip.invalid_source"),
  SKIP_OVER_SKIPS("SRS-9004", "error.skip.over_maximum_number_delays"),
  SKIP_OVER_CONSECUTIVE_DELAYS("SRS-9005",
      "error.skip.over_maximum_number_consecutive_delays"),
  SOURCE_INVALID("SRS-9006", "error.skip.source_invalid"),
  SKIP_STATUS_NOT_ACCEPTED("SRS-9007", "error.skip.not_accepted"),
  SKIP_CAN_NOT_SKIPPED("SRS-9008", "error.skip.sub_can_not_skipped"),
  SKIP_OVER_CONSECUTIVE_SKIPS("SRS-9009",
      "error.skip.over_maximum_number_consecutive_skips"),

  BREADCRUMB_RELATION_NOT_FOUND("SRS-10001", "error.breadcrumb.relation_not_found");

  private final String errorCode;
  private final String messageKey;
}
