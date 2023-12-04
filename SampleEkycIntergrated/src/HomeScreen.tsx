import React, {PropsWithChildren} from 'react';
import {
  NativeModules,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {useNavigation} from '@react-navigation/native';
import {NativeStackNavigationProp} from '@react-navigation/native-stack';
import {AppStackParamList} from './Routes';

export type LogResult = {
  logOcr?: string;
  logLivNessCardFront?: string;
  logLiveNessCardRear?: string;
  logCompare?: string;
  logLiveNessFace?: string;
  logMaskFace?: string;
};

const resultEKYCToLogResult = (result: any): LogResult => {
  return {
    logOcr: result.LOG_OCR,
    logLivNessCardFront: result.LOG_LIVENESS_CARD_FRONT,
    logLiveNessCardRear: result.LOG_LIVENESS_CARD_REAR,
    logCompare: result.LOG_COMPARE,
    logLiveNessFace: result.LOG_LIVENESS_FACE,
    logMaskFace: result.LOG_MASK_FACE,
  };
};

type ButtonProps = PropsWithChildren<{
  title: string;
  event: () => void;
}>;

const Button = ({title, event}: ButtonProps): React.JSX.Element => {
  return (
    <TouchableOpacity style={styles.sectionButton} onPress={event}>
      <Text style={styles.sectionButtonTitle}>{title}</Text>
    </TouchableOpacity>
  );
};

const isNullOrUndefined = (value?: string): boolean => {
  return value === null || value === undefined || value === 'undefined';
};

const isResultLogValid = (result: LogResult): boolean => {
  return (
    !isNullOrUndefined(result.logOcr) ||
    !isNullOrUndefined(result.logCompare) ||
    !isNullOrUndefined(result.logMaskFace) ||
    !isNullOrUndefined(result.logLiveNessFace) ||
    !isNullOrUndefined(result.logLiveNessCardRear) ||
    !isNullOrUndefined(result.logLivNessCardFront)
  );
};

const HomeScreen = (): React.JSX.Element => {
  const navigation =
    useNavigation<NativeStackNavigationProp<AppStackParamList>>();

  const navigateToLog = (result: any) => {
    const logResult = resultEKYCToLogResult(JSON.parse(result));
    if (isResultLogValid(logResult)) {
      navigation.navigate('Log', logResult);
    }
  };

  const openEKYCFull = async (): Promise<void> => {
    navigateToLog(await NativeModules.EkycBridge.startEkycFull());
  };

  const openEKYCOcr = async (): Promise<void> => {
    navigateToLog(await NativeModules.EkycBridge.startEkycOcr());
  };

  const openEKYCFace = async (): Promise<void> => {
    navigateToLog(await NativeModules.EkycBridge.startEkycFace());
  };

  const backgroundStyle = {
    backgroundColor: Colors.white,
    flex: 1,
  };

  // @ts-ignore
  return (
    <SafeAreaView style={backgroundStyle}>
      <View style={styles.spacer} />
      <View style={{height: NativeModules.StatusBarManager.HEIGHT}}>
        <Text style={styles.sectionTitle}>Tích hợp SDK VNPT eKYC</Text>
      </View>
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Button title="eKYC luồng đầy đủ" event={openEKYCFull} />
        <Button title="Thực hiện OCR giấy tờ" event={openEKYCOcr} />
        <Button title="Thực hiện kiểm tra khuôn mặt" event={openEKYCFace} />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionButton: {
    marginHorizontal: 16,
    marginTop: 16,
    paddingVertical: 8,
    paddingHorizontal: 16,
    borderRadius: 8,
    backgroundColor: '#18D696',
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: Colors.black,
    textAlign: 'center',
  },
  sectionButtonTitle: {
    fontSize: 17,
    fontWeight: '600',
    color: Colors.white,
    textAlign: 'center',
  },
  spacer: {
    height: 8,
  },
});

export default HomeScreen;
