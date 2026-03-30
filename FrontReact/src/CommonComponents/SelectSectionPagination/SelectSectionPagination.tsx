// react-router
import { 
    Route, 
    Routes 
} from "react-router-dom"

// (sub)component(s)
import NonogramSelectSectionPagination from "PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSelectSectionPagination/NonogramSelectSectionPagination"

const SelectSectionPagination = () => {
    return (
        <Routes>
            <Route path="nonogram" element={<NonogramSelectSectionPagination />}/>
        </Routes>
    )
}

export default SelectSectionPagination