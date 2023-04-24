import "./App.css"
import AppHeader  from "./CommonComponents/AppHeader/AppHeader"
import AppFooter from "./CommonComponents/AppFooter/AppFooter"

import { Provider } from "react-redux"
import { store } from "./store"

import { Outlet } from "react-router-dom"

function App() {
  return (
    <Provider store={store}>
      <div className="App">
        <AppHeader key={"app-header"}/>
        <Outlet/>
        <AppFooter/>
      </div>
    </Provider>
  )
}

export default App
