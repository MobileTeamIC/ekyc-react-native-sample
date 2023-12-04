import React, {PropsWithChildren} from 'react';
import {
  Clipboard,
  NativeModules,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {useNavigation, useRoute} from '@react-navigation/native';
import {LogResult} from './HomeScreen';

type LogProps = PropsWithChildren<{
  title: string;
  json?: string;
}>;

const copyToClipboard = (value: string): void => {
  Clipboard.setString(value);
};

const LogItem = ({title, json}: LogProps): React.JSX.Element => {
  return json != null ? (
    <View style={styles.logContainer}>
      <View style={styles.logActionHeader}>
        <Text style={styles.logTitle}>{title}</Text>
        {JSON.parse(json).hasOwnProperty('logID') && (
          <TouchableOpacity
            onPress={_ => copyToClipboard(JSON.parse(json).logID)}>
            <Text style={[styles.sectionAction, {color: Colors.white}]}>
              Copy logID
            </Text>
          </TouchableOpacity>
        )}
        <TouchableOpacity onPress={_ => copyToClipboard(json)}>
          <Text style={[styles.sectionAction, {color: Colors.white}]}>
            Copy
          </Text>
        </TouchableOpacity>
      </View>
      <Text style={styles.logText}>
        {JSON.stringify(JSON.parse(json.toString()), null, '\t')}
      </Text>
    </View>
  ) : (
    <View />
  );
};

const LogScreen = (): React.JSX.Element => {
  const route = useRoute();
  const navigation = useNavigation();
  const params: LogResult = route.params as LogResult;
  console.log(params);
  const copyAll = (value: LogResult) => {
    if (value.logOcr != null) {
      copyToClipboard(value.logOcr);
    }
    if (value.logCompare != null) {
      copyToClipboard(value.logCompare);
    }
    if (value.logLiveNessFace != null) {
      copyToClipboard(value.logLiveNessFace);
    }
    if (value.logMaskFace != null) {
      copyToClipboard(value.logMaskFace);
    }
    if (value.logLivNessCardFront != null) {
      copyToClipboard(value.logLivNessCardFront);
    }
    if (value.logLiveNessCardRear != null) {
      copyToClipboard(value.logLiveNessCardRear);
    }
  };

  const backgroundStyle = {
    backgroundColor: Colors.white,
    flex: 1,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <View style={styles.spacer} />
      <View style={styles.statusBar}>
        <TouchableOpacity onPress={_ => copyAll(params)}>
          <Text style={styles.sectionAction}>Copy All</Text>
        </TouchableOpacity>
        <Text style={styles.sectionTitle}>Hiển thị kết quả</Text>
        <TouchableOpacity onPress={_ => navigation.goBack()}>
          <Text style={styles.sectionAction}>Đóng</Text>
        </TouchableOpacity>
      </View>
      <ScrollView>
        <LogItem title="OCR" json={params.logOcr} />
        <LogItem
          title="Liveness Card Front"
          json={params.logLivNessCardFront}
        />
        <LogItem title="Liveness Card Rear" json={params.logLiveNessCardRear} />
        <LogItem title="Compare" json={params.logCompare} />
        <LogItem title="Liveness Face" json={params.logLiveNessFace} />
        <LogItem title="Mask Face" json={params.logMaskFace} />
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: Colors.black,
    textAlign: 'center',
    flex: 1,
    marginHorizontal: 8,
  },
  sectionAction: {
    marginHorizontal: 8,
    padding: 6,
    fontSize: 16,
    fontWeight: '600',
    color: Colors.black,
    textAlign: 'center',
  },
  logContainer: {
    flexDirection: 'column',
  },
  logActionHeader: {
    flexDirection: 'row',
    backgroundColor: '#18D696',
    padding: 8,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logTitle: {
    fontSize: 16,
    color: Colors.white,
    flex: 1,
  },
  logText: {
    fontSize: 14,
    padding: 8,
    color: Colors.black,
  },
  statusBar: {
    height: NativeModules.StatusBarManager.HEIGHT,
    backgroundColor: Colors.white,
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  spacer: {
    height: 8,
  },
});

export default LogScreen;
