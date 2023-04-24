import { Route, Routes } from "react-router-dom"

// (sub)components
import NonogramSelectSectionPagination from "../../PuzzleSpecific/NonogramContent/NonogramSelectSection/NonogramSelectSectionPagination/NonogramSelectSectionPagination"

const SelectSectionPagination = () => {
    return (
        <Routes>
            <Route path="nonogram" element={<NonogramSelectSectionPagination />}/>
        </Routes>
    )
}

export default SelectSectionPagination