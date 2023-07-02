import MainLayout from "@/pages/main/layout";
import {Button, Card, Col, Form, Input, notification, Row} from "antd";
import {useState} from "react";
import axios from "axios";
import RequestMethod, {selectMethod} from "@/pages/main/components/common";

function AssignRole2Resource() {

    const [notificationApi, contextHolder] = notification.useNotification();
    const [isAssignLoading, setIsAssignLoading] = useState(false)
    const [isRemoveLoading, setIsRemoveLoading] = useState(false)

    const notificationSuccess = (content) => {
        notificationApi.success({
            message: "Notification",
            description: content
        })
    }

    const notificationWarning = (content) => {
        notificationApi.warning({
            message: "Notification",
            description: content
        })
    }

    const assignSubmit = (values) => {
        setIsAssignLoading(true)
        axios.post(
            "/api/resource/role",
            {
                roleName: values.roleName,
                method: values.method,
                path: values.path
            },
            {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            }
        ).then(resp => {
            if (resp.data != null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationSuccess("Assign Successfully")
                } else {
                    notificationWarning(message)
                }
            }
            setIsAssignLoading(false);
        }).catch(err => {
            console.log(err)
        })
    }

    const removeSubmit = (values) => {
        setIsRemoveLoading(true)
        axios.delete(
            "/api/resource/role?roleName=" + values.roleName + "&method=" + values.method + "&path=" + values.path
        ).then(resp => {
            if (resp.data != null) {
                const {code, message} = resp.data
                if (code === 200) {
                    notificationSuccess("Remove Successfully")
                } else {
                    notificationWarning(message)
                }
            }
            setIsRemoveLoading(false);
        }).catch(err => {
            console.log(err)
        })
    }

    return (
        <>
            {contextHolder}
            <Row>
                <Col span={24}>
                    <Card title="Assign Role">
                        <Row>
                            <Col span={12}>
                                <Form
                                    name="assignRoleForm"
                                    onFinish={assignSubmit}
                                    autoComplete="off"
                                    labelCol={{
                                        span: 8,
                                    }}
                                    wrapperCol={{
                                        span: 16,
                                    }}
                                >
                                    <Form.Item
                                        label="Assign Role"
                                        name="roleName"
                                        rules={[{required: true, message: 'Please input role name!'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Form.Item
                                        label="to Resource method:"
                                        name="method"
                                        rules={[{required: true, message: 'Please input method!'}]}
                                    >
                                        {selectMethod}
                                    </Form.Item>
                                    <Form.Item
                                        label="path: "
                                        name="path"
                                        rules={[{required: true, message: 'Please input path!'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Form.Item
                                        wrapperCol={{
                                            offset: 8,
                                            span: 16,
                                        }}
                                    >
                                        <Button type="primary" htmlType="submit" disabled={isAssignLoading}>
                                            Submit
                                        </Button>
                                    </Form.Item>
                                </Form>
                            </Col>
                            <Col span={12}></Col>
                        </Row>
                    </Card>
                </Col>
            </Row>
            <Row>
                <Col span={24}>
                    <Card title="Remove Role">
                        <Row>
                            <Col span={15}>
                                <Form
                                    name="removeRoleForm"
                                    onFinish={removeSubmit}
                                    autoComplete="off"
                                    labelCol={{
                                        span: 8,
                                    }}
                                    wrapperCol={{
                                        span: 16,
                                    }}
                                >
                                    <Form.Item
                                        label="Remove Role"
                                        name="roleName"
                                        rules={[{required: true, message: 'Please input role name!'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Form.Item
                                        label="from Resource method: "
                                        name="method"
                                        rules={[{required: true, message: 'Please input method!'}]}
                                    >
                                        {selectMethod}
                                    </Form.Item>
                                    <Form.Item
                                        label="path: "
                                        name="path"
                                        rules={[{required: true, message: 'Please input path!'}]}
                                    >
                                        <Input/>
                                    </Form.Item>
                                    <Form.Item
                                        wrapperCol={{
                                            offset: 8,
                                            span: 16,
                                        }}
                                    >
                                        <Button type="primary" htmlType="submit" disabled={isRemoveLoading}>
                                            Submit
                                        </Button>
                                    </Form.Item>
                                </Form>
                            </Col>
                            <Col span={9}></Col>
                        </Row>
                    </Card>
                </Col>
            </Row>
        </>
    )
}

AssignRole2Resource.pageLayout = MainLayout

export default AssignRole2Resource