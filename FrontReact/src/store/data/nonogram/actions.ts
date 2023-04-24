import * as nonogramTypes from "./types"

export const SetNonogramsList = (nonogramsList : nonogramTypes.nonogramInformation[]) => {
    return {
        type: nonogramTypes.SET_NONOGRAMS_LIST,
        payload: {
            nonogramsList
        }
    }
}

export const SetSelectedNonogram = (updatedNonogram : nonogramTypes.selectedNonogramDetails | null) => {
    return {
        type: nonogramTypes.SET_SELECTED_NONOGRAM,
        payload: {
            updatedNonogram
        }
    }
}

export const SetEditedSquare = (squareEdit : nonogramTypes.nonogramSquareEdit) => {
    return {
        type: nonogramTypes.SET_EDITED_SQUARE,
        payload: {
            squareEdit
        }
    }
}

export const ResetEditedSquare = () => {
    return {
        type: nonogramTypes.SET_EDITED_SQUARE,
        payload: {
            squareEdit: nonogramTypes.nonSelectedSquareEdit
        }
    }
}

export const ModifySequence = (value: number, section: "rowSequences" | "columnSequences", index: number, seqNo: number) => {
    return {
        type: nonogramTypes.MODIFY_SEQUENCE,
        payload: {
            value,
            section,
            index,
            seqNo
        }
    }
}

export const ModifyRow = (rowIndex: number, rowSequences: Array<number>) => {
    return {
        type: nonogramTypes.MODIFY_ROW,
        payload: {
            rowIndex,
            rowSequences
        }
    }
}

export const ModifyColumn = (columnIndex: number, columnSequences: Array<number>) => {
    return {
        type: nonogramTypes.MODIFY_COLUMN,
        payload: {
            columnIndex,
            columnSequences
        }
    }
}

export const PreviewNonogram = () => {
    return {
        type: nonogramTypes.PREVIEW_NONOGRAM
    }
}

export const ClosePreviewNonogram = () => {
    return {
        type: nonogramTypes.CLOSE_PREVIEW_NONOGRAM
    }
}

export const SetCorrectness = (correct : nonogramTypes.correctnessIndicator) => {
    return {
        type: nonogramTypes.SET_CORRECTNESS,
        payload: {
            correct
        }
    }
}

export const AddNonogramToList = (nonogram : nonogramTypes.nonogramInformation) => {
    return {
        type: nonogramTypes.ADD_TEMPORARY_NONOGRAM,
        payload: {
            nonogram
        }
    }
}

export const AddNonogramDetail = (detail : keyof nonogramTypes.nonogramInformation) => {
    return {
        type: nonogramTypes.ADD_NONOGRAM_DETAIL,
        payload: {
            detail
        }
    }
}

export const RemoveNonogramDetail = (detail : keyof nonogramTypes.nonogramInformation) => {
    return {
        type: nonogramTypes.REMOVE_NONOGRAM_DETAIL,
        payload: {
            detail
        }
    }
}

export const ChangeNonogramDetail = (detail : keyof nonogramTypes.nonogramInformation, value : string | number | number[]) => {
    return {
        type: nonogramTypes.CHANGE_NONOGRAM_STRING_DETAIL,
        payload: {
            detail,
            value
        }
    }
}

export const SaveTemporaryNonogram = () => {
    return {
        type: nonogramTypes.SAVE_TEMPORARY_NONOGRAM
    }
}

export const AddRows = (startRowAddIdx: number, endRowAddIdx: number) => {
    return {
        type: nonogramTypes.ADD_ROWS,
        payload: {
            startRowAddIdx,
            endRowAddIdx
        }
    }
}

export const RemoveRows = (startRowRemoveIdx: number, endRowRemoveIdx: number) => {
    return {
        type: nonogramTypes.REMOVE_ROWS,
        payload: {
            startRowRemoveIdx,
            endRowRemoveIdx
        }
    }
}

export const AddColumns = (startColumnAddIdx: number, endColumnAddIdx: number) => {
    return {
        type: nonogramTypes.ADD_COLUMNS,
        payload: {
            startColumnAddIdx,
            endColumnAddIdx
        }
    }
}

export const RemoveColumns = (startColumnRemoveIdx: number, endColumnRemoveIdx: number) => {
    return {
        type: nonogramTypes.REMOVE_COLUMNS,
        payload: {
            startColumnRemoveIdx,
            endColumnRemoveIdx
        }
    }
}

export const SetEditMode = (editMode: boolean) => {
    return {
        type: nonogramTypes.SET_EDIT_MODE,
        payload: {
            editMode
        }
    }
}

export const ToggleMarksVisibility = (marksVisibility : boolean) => {
    return {
        type: nonogramTypes.TOGGLE_MARKS_VISIBILITY,
        payload: {
            marksVisibility
        }
    }
}

export const ToggleXsVisibility = (xsVisibility : boolean) => {
    return {
        type: nonogramTypes.TOGGLE_XS_VISIBILITY,
        payload: {
            xsVisibility
        }
    }
}