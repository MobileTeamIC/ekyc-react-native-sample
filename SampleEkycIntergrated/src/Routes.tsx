import * as React from 'react';
import {
  NavigationContainer,
  RouteConfig,
  StackNavigationState,
} from '@react-navigation/native';
import {
  createNativeStackNavigator,
  NativeStackNavigationEventMap,
  NativeStackNavigationOptions,
} from '@react-navigation/native-stack';
import {SafeAreaView, StatusBar, useColorScheme} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import HomeScreen, {LogResult} from './HomeScreen';
import LogScreen from './LogScreen';

// stack param list type
export type AppStackParamList = {
  Home: undefined;
  Log: LogResult | undefined;
};

// type of the single route in app stack
type AppStackRoutesType = RouteConfig<
  AppStackParamList,
  keyof AppStackParamList,
  StackNavigationState<AppStackParamList>,
  NativeStackNavigationOptions,
  NativeStackNavigationEventMap
>;

// strictly typed array of app stack routes
const appStackRoutes: Array<AppStackRoutesType> = [
  {
    name: 'Home',
    component: HomeScreen,
  },
  {
    name: 'Log',
    component: LogScreen,
  },
];

const Stack = createNativeStackNavigator<AppStackParamList>();

const App = (): React.JSX.Element => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: Colors.white,
    flex: 1,
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <NavigationContainer>
        <Stack.Navigator
          initialRouteName="Home"
          screenOptions={{
            headerShown: false,
          }}>
          {appStackRoutes.map(stackRoute => (
            <Stack.Screen key={stackRoute.name} {...stackRoute} />
          ))}
        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaView>
  );
};

export default App;
