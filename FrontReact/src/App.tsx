// react-router
import { Outlet } from "react-router-dom"

// redux
import { Provider } from "react-redux"

// redux - store
import { store } from "./store"

// (sub)component(s)
import AppHeader  from "./CommonComponents/AppHeader/AppHeader"
import AppFooter from "./CommonComponents/AppFooter/AppFooter"


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
