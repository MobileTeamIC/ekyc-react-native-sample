//
//  EkycBridgeModule.m
//  SampleEkycIntergrated
//
//  Created by Longcon99 on 30/05/2023.
//

#import <Foundation/Foundation.h>
#import "EkycBridgeModule.h"
#import "ICSdkEKYC/ICSdkEKYC.h"


@implementation EkycBridgeModule

// To export a module named RCTCalendarModule
RCT_EXPORT_MODULE(EkycBridge);

RCT_EXPORT_METHOD(startEkycFull:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  NSLog(@"Hello world");
  
  self._resolve = resolve;
  self._reject = reject;
  
  ICEkycCameraViewController *camera = (ICEkycCameraViewController *) [ICEkycCameraRouter createModule];
  camera.cameraDelegate = self;
  
  /// Giá trị này xác định kiểu giấy tờ để sử dụng:
  /// - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
  /// - IDCardChipBased: Căn cước công dân gắn Chip
  /// - Passport: Hộ chiếu
  /// - DriverLicense: Bằng lái xe
  /// - MilitaryIdCard: Chứng minh thư quân đội
  camera.documentType = IdentityCard;
  
  /// Luồng đầy đủ
  /// Bước 1 - chụp ảnh giấy tờ
  /// Bước 2 - chụp ảnh chân dung xa gần
  camera.flowType = full;
  
  /// xác định xác thực khuôn mặt bằng oval xa gần
  camera.versionSdk = ProOval;
  
  /// Bật/Tắt chức năng So sánh ảnh trong thẻ và ảnh chân dung
  camera.isCompareFaces = YES;
  
  /// Bật/Tắt chức năng kiểm tra che mặt
  camera.isCheckMaskedFace = YES;
  
  /// Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
  camera.isCheckLivenessCard = YES;
  
  /// Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
  /// - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
  /// - Basic: Kiểm tra sau khi chụp ảnh
  /// - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
  /// - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
  camera.validateDocumentType = Basic;
  
  /// Giá trị này xác định việc có xác thực số ID với mã tỉnh thành, quận huyện, xã phường tương ứng hay không.
  camera.isValidatePostcode = YES;
  
  /// Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
  /// - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
  /// - iBETA: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
  /// - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
  camera.checkLivenessFace = IBeta;
  
  /// Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
  camera.challengeCode = @"INNOVATIONCENTER";
  
  /// Ngôn ngữ sử dụng trong SDK
  /// - vi: Tiếng Việt
  /// - en: Tiếng Anh
  camera.languageSdk = @"vi";
  
  /// Bật/Tắt Hiển thị màn hình hướng dẫn
  camera.isShowTutorial = YES;
  
  /// Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
  camera.isEnableGotIt = YES;
  
  /// Sử dụng máy ảnh mặt trước
  /// - PositionFront: Camera trước
  /// - PositionBack: Camera sau
  camera.cameraPositionForPortrait = PositionFront;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    BOOL modalPresent = (BOOL) (root.presentedViewController);
    
    if (modalPresent) {
      UIViewController *parent = root.presentedViewController;
      [parent setModalPresentationStyle:UIModalPresentationFullScreen];
      [parent showViewController:camera sender:parent];
      
    } else {
      [camera setModalPresentationStyle:UIModalPresentationFullScreen];
      [root showDetailViewController:camera sender:root];
    }
    
  });

};


RCT_EXPORT_METHOD(startEkycOcr:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  NSLog(@"Hello world");
  
  self._resolve = resolve;
  self._reject = reject;
  
  ICEkycCameraViewController *camera = (ICEkycCameraViewController *) [ICEkycCameraRouter createModule];
  camera.cameraDelegate = self;
  
  /// Giá trị này xác định kiểu giấy tờ để sử dụng:
  /// - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
  /// - IDCardChipBased: Căn cước công dân gắn Chip
  /// - Passport: Hộ chiếu
  /// - DriverLicense: Bằng lái xe
  /// - MilitaryIdCard: Chứng minh thư quân đội
  camera.documentType = IdentityCard;
  
  /// Luồng đầy đủ
  /// Bước 1 - chụp ảnh giấy tờ
  /// Bước 2 - chụp ảnh chân dung xa gần
  camera.flowType = ocr;
  
  /// Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
  camera.isCheckLivenessCard = YES;
  
  /// Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
  /// - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
  /// - Basic: Kiểm tra sau khi chụp ảnh
  /// - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
  /// - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
  camera.validateDocumentType = Basic;
  
  /// Giá trị này xác định việc có xác thực số ID với mã tỉnh thành, quận huyện, xã phường tương ứng hay không.
  camera.isValidatePostcode = YES;
  
  /// Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
  camera.challengeCode = @"INNOVATIONCENTER";
  
  /// Ngôn ngữ sử dụng trong SDK
  /// - vi: Tiếng Việt
  /// - en: Tiếng Anh
  camera.languageSdk = @"vi";
  
  /// Bật/Tắt Hiển thị màn hình hướng dẫn
  camera.isShowTutorial = YES;
  
  /// Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
  camera.isEnableGotIt = YES;
  
  /// Sử dụng máy ảnh mặt trước
  /// - PositionFront: Camera trước
  /// - PositionBack: Camera sau
  camera.cameraPositionForPortrait = PositionFront;
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    BOOL modalPresent = (BOOL) (root.presentedViewController);
    
    if (modalPresent) {
      UIViewController *parent = root.presentedViewController;
      [parent setModalPresentationStyle:UIModalPresentationFullScreen];
      [parent showViewController:camera sender:parent];
      
    } else {
      [camera setModalPresentationStyle:UIModalPresentationFullScreen];
      [root showDetailViewController:camera sender:root];
    }
    
  });

};


RCT_EXPORT_METHOD(startEkycFace:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject) {
  NSLog(@"Hello world");
  
  self._resolve = resolve;
  self._reject = reject;
  
  ICEkycCameraViewController *camera = (ICEkycCameraViewController *) [ICEkycCameraRouter createModule];
  camera.cameraDelegate = self;
  
  /// Giá trị này xác định kiểu giấy tờ để sử dụng:
  /// - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
  /// - IDCardChipBased: Căn cước công dân gắn Chip
  /// - Passport: Hộ chiếu
  /// - DriverLicense: Bằng lái xe
  /// - MilitaryIdCard: Chứng minh thư quân đội
  camera.documentType = IdentityCard;
  
  /// Luồng đầy đủ
  /// Bước 1 - chụp ảnh giấy tờ
  /// Bước 2 - chụp ảnh chân dung xa gần
  camera.flowType = face;
  
  /// xác định xác thực khuôn mặt bằng oval xa gần
  camera.versionSdk = ProOval;
  
  /// Bật/Tắt chức năng So sánh ảnh trong thẻ và ảnh chân dung
  camera.isCompareFaces = YES;
  
  /// Bật/Tắt chức năng kiểm tra che mặt
  camera.isCheckMaskedFace = YES;
  
  /// Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
  /// - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
  /// - iBETA: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
  /// - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
  camera.checkLivenessFace = IBeta;
  
  /// Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
  camera.challengeCode = @"INNOVATIONCENTER";
  
  /// Ngôn ngữ sử dụng trong SDK
  /// - vi: Tiếng Việt
  /// - en: Tiếng Anh
  camera.languageSdk = @"vi";
  
  /// Bật/Tắt Hiển thị màn hình hướng dẫn
  camera.isShowTutorial = YES;
  
  /// Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
  camera.isEnableGotIt = YES;
  
  /// Sử dụng máy ảnh mặt trước
  /// - PositionFront: Camera trước
  /// - PositionBack: Camera sau
  camera.cameraPositionForPortrait = PositionFront;
  
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIViewController *root = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    BOOL modalPresent = (BOOL) (root.presentedViewController);
    
    if (modalPresent) {
      UIViewController *parent = root.presentedViewController;
      [parent setModalPresentationStyle:UIModalPresentationFullScreen];
      [parent showViewController:camera sender:parent];
      
    } else {
      [camera setModalPresentationStyle:UIModalPresentationFullScreen];
      [root showDetailViewController:camera sender:root];
    }
    
  });

};


-(void) initParamSdk {
  
  ICEKYCSavedData.shared.tokenKey = @"";
  ICEKYCSavedData.shared.tokenId = @"";
  ICEKYCSavedData.shared.authorization = @"";

}


#pragma mark - Delegate
- (void)icEkycGetResult {
  NSLog(@"Finished SDK");
  
  NSString* dataInfoResult = ICEKYCSavedData.shared.ocrResult;
  NSString* dataLivenessCardFrontResult = ICEKYCSavedData.shared.livenessCardFrontResult;
  NSString* dataLivenessCardRearResult = ICEKYCSavedData.shared.livenessCardBackResult;
  NSString* dataCompareResult = ICEKYCSavedData.shared.compareFaceResult;
  NSString* dataLivenessFaceResult = ICEKYCSavedData.shared.livenessFaceResult;
  NSString* dataMaskedFaceResult = ICEKYCSavedData.shared.maskedFaceResult;
  
  NSDictionary* dict = @{
    @"LOG_OCR": dataInfoResult,
    @"LOG_LIVENESS_CARD_FRONT": dataLivenessCardFrontResult,
    @"LOG_LIVENESS_CARD_REAR": dataLivenessCardRearResult,
    @"LOG_COMPARE": dataCompareResult,
    @"LOG_LIVENESS_FACE": dataLivenessFaceResult,
    @"LOG_MASK_FACE": dataMaskedFaceResult
  };
  
  NSError* error;
  NSData* data= [NSJSONSerialization dataWithJSONObject:dict options:0 error:&error];
  
  NSString* resultJson = @"";
  if (error) {
    NSLog(@"Failure to serialize JSON object %@", error);
    
  } else {
    resultJson = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  }
  
  self._resolve(resultJson);
  // self._resolve = nil;
  
}

- (void)icEkycCameraClosedWithType:(ScreenType)type {
  NSLog(@"icEkycCameraClosedWithType SDK");

}

@end
