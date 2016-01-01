package com.example.admin.oneclickwash.Constant;

import com.example.admin.oneclickwash.R;

/**
 * Created by Admin on 12/1/2015.
 */
public class Constant {

    public static  String STREMAILADDREGEX="^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; //EMAIL REGEX
    public static String PREFS_NAME="MyPrefsFile";

    public static final String MSG_KEY = "m";

    public static String  TITLES[]={"Price List","Bookings","Notification","Refer & Earn","Recharge Wallet","Terms & Conditions","Rate Us","Contact Us","Support","Sign Out"};
    public static int ICONS[]={R.drawable.dpayment,R.drawable.dbooking,R.drawable.dnotification,R.drawable.dinvite,R.drawable.dwallet,R.drawable.dedit_profile,R.drawable.drating,R.drawable.dcontact_us,R.drawable.daboutus,R.drawable.dsignout};

    public static String NAME = "";
    public static String EMAIL = "";
    public static String PROFILE ="";
    public static String WALLET ="";

    public static final String GOOGLE_PROJ_ID ="35388647227";
    //-t279vbsadusleunkp7coo1d7p1v30occ.apps.googleusercontent.com

    public static String SP_USER_ID="user_id";
    public static String SP_DEVICE_ID="device_id";
    public static String SP_GCM_ID="gcm_id";

    public static String ServiceNormal="Wash + Dry + Iron";
    public static String ServiceSpecial="Dry Cleaning";

    public static String statusPickup="Pickup Scheduled";
    public static String statusRecieved="Recieved at facility";
    public static String statusProgress="Order in progress";
    public static String statusReady="Order Ready";
    public static String statusDelivery="Delivery Scheduled";
    public static String statusComplete="Completed";
    public static String statusCancel="Cancelled";
    public static String statusPickupFailed="Pickup Failed";




    //http://oneclickwash.com/webservices/signup.php?mobile=&email=&city=&address=&password=&gcm_id=&otp=
    public static String WS_HTTP="Http";
    public static String WS_DOMAIN_NAME="oneclickwash.in";
    public static String WS_PATH="/webservices/";

    public static String STR_OTP;

    public static String WS_RESPONSE_SUCCESS="success";
    public static String WS_RESPONSE_FAILURE="failure";
    public static String WS_RESPONSE_OTP="otp";
    public static String WS_RESPONSE_Exist="exist";
    public static String WS_RESPONSE_SCHEDULED="scheduled";

    public static String WS_SUBSCRIPTION_PACKAGE="subscription_details.php";
    public static String WS_USER_DETAILS="user_detail.php";
    public static String WS_SUBSCRIPTION_DONE="user_subscribe.php";
    public static String WS_SIGNUP="signup.php";
    public static String WS_CHANGE="password_update.php";
    public static String WS_FORGOT="forgot_password.php";
    public static String WS_PRICE_LIST="subscription_details.php";
    public static String WS_HELP="help.php";
    public static String WS_GET_REFER="referral_details.php";
    public static String WS_TIME_SLOT="pick_slot_details.php";
    public static String WS_DROP_SLOT="drop_slot_details.php";
    public static String WS_PLACE_ORDER="place_order.php";
    public static String WS_UPDATE_PROFILE="update_profile.php";
    public static String WS_BOOKING_LIST="booking_list.php";
    public static String WS_BOOKING_DETAIL="booking_details.php";
    public static String WS_FEEDBACK="feedback.php";
    public static String WS_VALIDATE_REFERRAL="check_promo.php";
    public static String WS_RESEND_OTP="resend_otp.php";
    public static String WS_OTP_VERIFY="otp_verify.php";
    public static String WS_BUT_CREDIT="buy.php";
    public static String WS_WALLET_AMOUNT="wallet.php";
    public static String WS_SUBSCRIPTION_DETAIL="subscription_view.php";
    public static String WS_SUBSCRIPTION_PAYMENT="buy_subscription.php";
    public static String WS_SUBSCRIPTION_CANCEL="cancel.php";
    public static String WS_SCHEDULE_DROP="dropoff_schedule.php";
    public static String WS_RESCHEDULE_DROP="dropoff_reschedule.php";
    public static String WS_RESCHEDULE_PICK="reschedule_pickup.php";
    public static String WS_CANCEL_PICK="cancel_pickup.php";
    public static String WS_CHANGE_ADDRESS="update_address.php";
    public static String WS_SUBSCRIPTION_OK="wallet_payment.php";
    public static String WS_NOTIFICATION_LIST="notification_list.php";



    public static String packageID[];
    public static String packageName[];
    public static String packagePrice[];
    public static String packageCloth[];
    public static String packagePickup[];

    public static String subscriptionActive="active";
    public static String subscriptionInActive="Inactive";


    /* monthly price list */

    public static String pricingID[];
    public static String pricingCloth[];
    public static String pricingUser[];
    public static String pricingPickup[];
    public static String pricingPackage[];
    public static String pricingOriginal[];

    /* dry clean price list */

    public static String dryCleanID[];
    public static String dryCleanItem[];
    public static String dryCleanPrice[];
    public static String dryCleanOriginal[];

    /* booking list */

    public static String bookingID[];
    public static String bookingdate[];
    public static String bookingPickdate[];
    public static String bookingPickTime[];
    public static String bookingStatus[];
    public static String bookingType[];
    public static String bookingDryclean[];
    public static int bookingWindow[];

    public static String bookingDropdate[];
    public static String bookingDropTime[];
    public static int bookingDropWindow[];

    /* notification list*/

    public static String notificationTitle[];
    public static String notificationMessage[];
    public static String notificationDate[];


}
