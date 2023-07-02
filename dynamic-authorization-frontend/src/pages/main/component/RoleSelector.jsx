import {Select} from "antd";
import {useEffect, useState} from "react";
import axios from "axios";

export default function RoleSelector({selectedItems, selectionChange}) {

    const [options, setOptions] = useState([])
    const [selectedRoles, setSelectedRoles] = useState([])

    useEffect(() => {
        axios.get(
            "/api/role/all"
        ).then(resp => {
            if (resp.data !== null) {
                const {data: roles} = resp.data
                const o = []
                roles.forEach(role => {
                    o.push({
                        value: role.name,
                        label: role.name
                    })
                })
                setOptions([...o])
            }
        }).catch(err => {
            console.log(err)
        })
    }, [])

    const selectChange = (values) => {
        // console.log(values)
        if (selectionChange !== undefined && selectionChange != null) {
            selectionChange(values)
        }
    }

    return (
        <>
            <Select
                mode="multiple"
                showSearch={false}
                placeholder="Please select"
                defaultValue={selectedItems}
                options={options}
                onChange={selectChange}
                style={{width: "100%"}}
            />
        </>
    )
}