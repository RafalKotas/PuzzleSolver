import * as React from "react"
import { Checkbox } from "@material-ui/core"
import TextField from "@mui/material/TextField"
import AutoComplete from "@mui/material/Autocomplete"
import CheckboxOutlineBlankIcon from "@mui/icons-material/CheckBoxOutlineBlank"
import CheckBoxIcon from "@mui/icons-material/CheckBox"

const icon = <CheckboxOutlineBlankIcon fontSize="small" />
const checkedIcon = <CheckBoxIcon fontSize="small" />

interface CheckBoxGroupProps {
  label: string
  options: Array<string>,
  selectedOptions: Array<string>,
  onHandleChange: (updatedList: Array<string>) => void
}

const CheckBoxGroup : React.FC<CheckBoxGroupProps> = ({label, options, selectedOptions, onHandleChange}) => {

    const handleOnCheckUpdate = (event: React.SyntheticEvent<Element, Event>, newValue: string[]) => {
        onHandleChange(newValue)
    }
    
    return (
        <AutoComplete
            limitTags={3}
            multiple
            id="checkboxes-test"
            options={options}
            value={selectedOptions}
            disableCloseOnSelect
            onChange={(event: React.SyntheticEvent<Element, Event>, newValue) => {
                handleOnCheckUpdate(event, newValue)
            }}
            renderOption={(props, option : string, { selected }) => (
                <li {...props}>
                    <Checkbox
                        icon={icon}
                        checkedIcon={checkedIcon}
                        value={option}
                        style={{marginTop: 5}}
                        checked={selectedOptions.includes(option)}
                    />
                    {option}
                </li>
            )}
            style={{ width: 300, marginTop: 5}}
            renderInput={(params) => (
                <TextField {...params} label={label}/>
            )}
        />
    )
}

export default CheckBoxGroup