import {Select} from "antd";

export default function ResourceAuthTypeSelector({selectedItem, selectionChange}) {

    const selectOnChange = (value) => {
        selectionChange(value)
    }

    return (
        <>
            <Select
                options={[
                    {
                        value: "PERMIT_ALL",
                        label: "PERMIT_ALL"
                    },
                    {
                        value: "JUST_AUTHENTICATION",
                        label: "JUST_AUTHENTICATION"
                    },
                    {
                        value: "ANY_ROLE",
                        label: "ANY_ROLE"
                    },
                    {
                        value: "ALL_ROLE",
                        label: "ALL_ROLE"
                    },
                ]}
                placeholder="Please select"
                defaultValue={selectedItem}
                onChange={selectOnChange}
                style={{width: "100%"}}
            />
        </>
    )
}