import {Select} from "antd";

export const selectMethod = (
    <Select
        options={[
            {
                value: "POST",
                label: "POST"
            },
            {
                value: "DELETE",
                label: "DELETE"
            },
            {
                value: "PUT",
                label: "PUT"
            },
            {
                value: "GET",
                label: "GET"
            }
        ]}
    />
)