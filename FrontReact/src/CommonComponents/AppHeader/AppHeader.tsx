import { NavLink, useLocation } from "react-router-dom"
import "./AppHeader.css"

const puzzlesNamesList : string[] = ["NONOGRAM", "SUDOKU", "SLITHERLINK", "AKARI", "HITORI", "ARCHITECT"]

const AppHeader = () => {

    let location = useLocation()

    return (
        <header id="app-header-puzzle-select">
            {
                puzzlesNamesList.map((puzzleName) => {
                    return (
                        <h3 key={puzzleName} className={"puzzle-page-link"}>
                            <NavLink
                                style={({ isActive }) => {
                                    return {
                                        color: (isActive || location.pathname.includes(puzzleName.toLowerCase())) ? "red" : "blue",
                                        fontWeight: (isActive || location.pathname.includes(puzzleName.toLowerCase())) ? "bold" : "",
                                        textDecoration: "none",
                                        letterSpacing: "1px"
                                    }
                                }}
                                to={puzzleName.toLowerCase()}
                                key={puzzleName.toLowerCase() + "-nav"}
                            >
                                {puzzleName}
                            </NavLink>
                        </h3>
                    )
                })
            }
        </header>
    )
}

export default AppHeader