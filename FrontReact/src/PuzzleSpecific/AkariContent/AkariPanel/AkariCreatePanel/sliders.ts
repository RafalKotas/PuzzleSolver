interface inputSliderProps {
    label: string,
    propertyName: string,
    minValue: number,
    maxValue: number,
    initialValue: number,
    step: number
}

export const sliders : inputSliderProps[] = [
    {
        label: "Difficulty",
        propertyName: "difficulty",
        minValue: 1.0,
        maxValue: 5.0,
        initialValue: 3.0,
        step: 0.1
    },
    {
        label: "Height",
        propertyName: "height",
        minValue: 7,
        maxValue: 30,
        initialValue: 20,
        step: 1
    },
    {
        label: "Width",
        propertyName: "width",
        minValue: 7,
        maxValue: 30,
        initialValue: 20,
        step: 1
    }
]