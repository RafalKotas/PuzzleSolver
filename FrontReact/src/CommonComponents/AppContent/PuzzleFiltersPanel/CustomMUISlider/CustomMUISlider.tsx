import Box from "@mui/material/Box/Box"
import Slider from "@mui/material/Slider"
import Typography from "@mui/material/Typography"
import React, {useEffect, useState} from "react"

interface CustomMUISliderProps {
    minValue: number,
    maxValue: number,
    step: number,
    filterLabel: string,
    onHandleChange: (updatedList: Array<number>) => void
}

const CustomMUISlider : React.FC<CustomMUISliderProps> = ({minValue, maxValue, step, filterLabel, onHandleChange}) => {
    
    const [value, setValue] = useState<number[]>([minValue, maxValue]);

    const handleChange = (event: Event, newValue: number | number[]) => {
        setValue(newValue as number[]);
    }

    useEffect(() => {
        onHandleChange(value)

        // eslint-disable-next-line
    }, [value])
    
    return (
        <React.Fragment>
            <Box sx={{ m: 3 }}>
                <Typography gutterBottom>{filterLabel}</Typography>
                <Slider
                    getAriaLabel={() => filterLabel}
                    max={maxValue}
                    min={minValue}
                    value={value}
                    step={step}
                    valueLabelDisplay={"auto"}
                    onChange={handleChange}
                    style={{width: 300}}
                />
            </Box>
        </React.Fragment>
    )
}

export default CustomMUISlider
