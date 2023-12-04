package com.sampleekycintergrated;

import static com.vnptit.idg.sdk.utils.KeyIntentConstants.ACCESS_TOKEN;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CAMERA_POSITION_FOR_PORTRAIT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHALLENGE_CODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHECK_LIVENESS_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_LIVENESS_CARD;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_MASKED_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_COMPARE_FLOW;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_GOT_IT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_SHOW_TUTORIAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_VALIDATE_POSTCODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.LANGUAGE_SDK;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_ID;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_KEY;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TYPE_VALIDATE_DOCUMENT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VERSION_SDK;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.INFO_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_REAR_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.JsonObject;
import com.vnptit.idg.sdk.activity.VnptIdentityActivity;
import com.vnptit.idg.sdk.activity.VnptOcrActivity;
import com.vnptit.idg.sdk.activity.VnptPortraitActivity;
import com.vnptit.idg.sdk.utils.SDKEnum;

public class EkycBridgeModule extends ReactContextBaseJavaModule {
   private static final int EKYC_REQUEST_CODE = 100;
   private static final String LOG_OCR = "LOG_OCR";
   private static final String LOG_LIVENESS_CARD_FRONT = "LOG_LIVENESS_CARD_RESULT";
   private static final String LOG_LIVENESS_CARD_REAR = "LOG_LIVENESS_CARD_REAR";
   private static final String LOG_COMPARE = "LOG_COMPARE";
   private static final String LOG_LIVENESS_FACE = "LOG_LIVENESS_FACE";
   private static final String LOG_MASK_FACE = "LOG_MASK_FACE";

   private Promise mEkycPromise;

   public EkycBridgeModule(ReactApplicationContext reactContext) {
      super(reactContext);

      final ActivityEventListener activityEventListener = new BaseActivityEventListener() {
         @Override
         public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (requestCode == EKYC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
               if (data != null &&  mEkycPromise != null) {
                  final String dataInfoResult = data.getStringExtra(INFO_RESULT);
                  final String dataLivenessCardFrontResult = data.getStringExtra(LIVENESS_CARD_FRONT_RESULT);
                  final String dataLivenessCardRearResult = data.getStringExtra(LIVENESS_CARD_REAR_RESULT);
                  final String dataCompareResult = data.getStringExtra(COMPARE_RESULT);
                  final String dataLivenessFaceResult = data.getStringExtra(LIVENESS_FACE_RESULT);
                  final String dataMaskedFaceResult = data.getStringExtra(MASKED_FACE_RESULT);

                  final JsonObject json = new JsonObject();
                  json.addProperty(LOG_OCR, dataInfoResult);
                  json.addProperty(LOG_LIVENESS_CARD_FRONT, dataLivenessCardFrontResult);
                  json.addProperty(LOG_LIVENESS_CARD_REAR, dataLivenessCardRearResult);
                  json.addProperty(LOG_COMPARE, dataCompareResult);
                  json.addProperty(LOG_LIVENESS_FACE, dataLivenessFaceResult);
                  json.addProperty(LOG_MASK_FACE, dataMaskedFaceResult);
                  mEkycPromise.resolve(json.toString());
               }

               mEkycPromise = null;
            }
         }
      };
      reactContext.addActivityEventListener(activityEventListener);
   }

   @NonNull
   @Override
   public String getName() {
      return "EkycBridge";
   }

   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh chân dung xa gần
   // Bước 2 - hiển thị kết quả
   @ReactMethod
   private void startEkycFace(final Promise promise) {
      final Activity currentActivity = getCurrentActivity();
      if (currentActivity == null) {
         return;
      }

      mEkycPromise = promise;

      final Intent intent = getBaseIntent(currentActivity, VnptPortraitActivity.class);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ADVANCED: chụp ảnh chân dung xa gần
      intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      // Bật/[Tắt] chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(IS_COMPARE_FLOW, false);

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - IBeta: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      currentActivity.startActivityForResult(intent, EKYC_REQUEST_CODE);
   }


   // Phương thức thực hiện eKYC luồng "Chụp ảnh giấy tờ"
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - hiển thị kết quả
   @ReactMethod
   private void startEkycOcr(final Promise promise) {
      final Activity currentActivity = getCurrentActivity();
      if (currentActivity == null) {
         return;
      }

      mEkycPromise = promise;

      final Intent intent = getBaseIntent(currentActivity, VnptOcrActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      currentActivity.startActivityForResult(intent, EKYC_REQUEST_CODE);
   }

   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - chụp ảnh chân dung xa gần
   // Bước 3 - hiển thị kết quả
   @ReactMethod
   private void startEkycFull(final Promise promise) {
      final Activity currentActivity = getCurrentActivity();
      if (currentActivity == null) {
         return;
      }

      mEkycPromise = promise;

      final Intent intent = getBaseIntent(currentActivity, VnptIdentityActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(IS_COMPARE_FLOW, true);

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - iBETA: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      // Giá trị này xác định việc có xác thực số ID với mã tỉnh thành, quận huyện, xã phường tương ứng hay không.
      intent.putExtra(IS_VALIDATE_POSTCODE, true);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ProOval: chụp ảnh chân dung xa gần
      intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      currentActivity.startActivityForResult(intent, EKYC_REQUEST_CODE);
   }

   private Intent getBaseIntent(final Activity activity, final Class<?> clazz) {
      final Intent intent = new Intent(activity, clazz);

      // Nhập thông tin bộ mã truy cập. Lấy tại mục Quản lý Token https://ekyc.vnpt.vn/admin-dashboard/console/project-manager
      intent.putExtra(ACCESS_TOKEN, "");
      intent.putExtra(TOKEN_ID, "");
      intent.putExtra(TOKEN_KEY, "");

      // Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
      intent.putExtra(CHALLENGE_CODE, "INNOVATIONCENTER");

      // Ngôn ngữ sử dụng trong SDK
      // - VIETNAMESE: Tiếng Việt
      // - ENGLISH: Tiếng Anh
      intent.putExtra(LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.getValue());

      // Bật/Tắt Hiển thị màn hình hướng dẫn
      intent.putExtra(IS_SHOW_TUTORIAL, true);

      // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
      intent.putExtra(IS_ENABLE_GOT_IT, true);

      // Sử dụng máy ảnh mặt trước
      // - FRONT: Camera trước
      // - BACK: Camera trước
      intent.putExtra(CAMERA_POSITION_FOR_PORTRAIT, SDKEnum.CameraTypeEnum.FRONT.getValue());

      return intent;
   }
}
