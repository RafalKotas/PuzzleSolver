import { sudokuInformation } from "./types"

export const listIncludesFileName = (sudokusList : sudokuInformation[], sudokuToAdd : sudokuInformation) => {
    return sudokusList.filter((sudokuDetails) => {
        return sudokuDetails.value === sudokuToAdd.value
    }).length > 0
}