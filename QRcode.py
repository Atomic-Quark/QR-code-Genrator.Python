import qrcode
from PIL import Image
class QRCodeGenerator:

	def __init__(self):
		self.size = 4
		self.border = 4
		self.fill_color = "black"
		self.back_color = "white"
		self.box_size = 10

	def set_properties(self, size=None, border=None, fill_color=None, back_color=None, box_size=None):
		if size is not None:
			self.size = size
		if border is not None:
			self.border = border
		if fill_color is not None:
			self.fill_color = fill_color
		if back_color is not None:
			self.back_color = back_color
		if box_size is not None:
			self.box_size = box_size

	def generate(self, data, output_file="qrcode5.png"):
		qr = qrcode.QRCode(
			version=self.size,
			error_correction=qrcode.constants.ERROR_CORRECT_H,
			box_size=self.box_size,
			border=self.border
		)

		qr.add_data(data)
		qr.make(fit=True)

		img = qr.make_image(fill_color=self.fill_color, back_color=self.back_color)

		img.save(output_file)

		return os.path.abspath(output_file)

if __name__ == "__main__":

	generator = QRCodeGenerator()

	output_path = generator.generate("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlYxTsrHiM5jFyET2Cec3NTSRY7vMKIsQ1XQ&s")
	print(f"QR code created at: {output_path}")
