import MainLayout from "@/pages/main/layout";
import {useState} from "react";
import {Button, Card, Col, Form, Input, notification, Row} from "antd";
import axios from "axios";

function AddAuthority() {
    const [loading, setLoading] = useState(false)
    const [notificationApi, contextHolder] = notification.useNotification()

    const addAuthoritySubmit = (values) => {
        setLoading(true)
        axios.post(
            "/api/authority",
            {
                name: values.name
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
                    notificationApi.success({
                        message: "Notification",
                        description: "Add New Role Successfully"
                    });
                } else {
                    notificationApi.warning({
                        message: "Notification",
                        description: message
                    })
                }
            }
            setLoading(false)
        }).catch(err => {
            console.log(err)
            setLoading(false)
        })
    }

    return (
        <>
            {contextHolder}
            <Card
                title="Add Role"
                bordered={false}
            >
                <Row>
                    <Col span={12}>
                        <Form
                            name="addAuthorityForm"
                            labelCol={{
                                span: 8,
                            }}
                            wrapperCol={{
                                span: 16,
                            }}
                            onFinish={addAuthoritySubmit}
                            autoComplete="off"
                        >
                            <Form.Item
                                label="Authority Name"
                                name="name"
                                rules={[
                                    {
                                        required: true,
                                        message: 'Please input authority name!',
                                    },
                                ]}
                            >
                                <Input />
                            </Form.Item>

                            <Form.Item
                                wrapperCol={{
                                    offset: 8,
                                    span: 16,
                                }}
                            >
                                <Button type="primary" htmlType="submit" disabled={loading}>
                                    Submit
                                </Button>
                            </Form.Item>
                        </Form>
                    </Col>
                    <Col span={12}></Col>
                </Row>
            </Card>
        </>
    )
}

AddAuthority.pageLayout = MainLayout

export default AddAuthority