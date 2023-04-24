//react(-router)
import { Outlet} from "react-router-dom"

//styles
import "./PuzzleFiltersPanel.css"


const PuzzleFiltersPanel = () => {
    return (
        <div id="puzzle-filters-panel">
            <Outlet />
        </div>

    )
}

export default PuzzleFiltersPanel